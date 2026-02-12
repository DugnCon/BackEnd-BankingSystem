package com.damdung.banking.config.filter;

import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    /**
     * ProxyManager là đối tượng trung gian làm việc với Redis.
     * Nó không lưu bucket trong JVM mà chỉ đại diện (proxy)
     * để tạo/lấy bucket đang được lưu trữ trong Redis.
     */
    @Autowired
    private ProxyManager<String> proxyManager;

    /**
     * BucketConfiguration định nghĩa luật rate limit:
     * - capacity (số request tối đa)
     * - refill (cách hồi token theo thời gian)
     * Cấu hình này chỉ được dùng khi bucket chưa tồn tại trong Redis.
     */
    @Autowired
    private BucketConfiguration bucketConfig;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        /**
         * Xác định key dùng để rate limit.
         * Nếu user đã đăng nhập thì dùng userId/username,
         * nếu chưa đăng nhập thì dùng IP.
         */
        String key = resolveKey(request);

        /**
         * Lấy hoặc tạo bucket tương ứng với key trong Redis.
         * Nếu Redis chưa có key này thì bucketConfig sẽ được dùng
         * để khởi tạo bucket mới.
         */
        var bucket = proxyManager.builder()
                .build(key, () -> bucketConfig);

        /**
         * Thử tiêu thụ 1 token cho request hiện tại.
         * - Nếu còn token: trả về true và cho request đi tiếp
         * - Nếu hết token: trả về false và request bị chặn
         */
        if (!bucket.tryConsume(1)) {
            /**
             * HTTP 429: Too Many Requests
             * Request bị từ chối do vượt quá giới hạn cho phép.
             */
            response.setStatus(429);
            response.getWriter().write("Quá nhiều yêu cầu");
            return;
        }

        /**
         * Cho phép request đi tiếp sang filter tiếp theo
         * hoặc controller nếu không còn filter nào phía sau.
         */
        filterChain.doFilter(request, response);
    }

    private String resolveKey(HttpServletRequest request) {

        /**
         * Lấy thông tin authentication hiện tại từ Spring Security.
         * Đối tượng này chỉ có giá trị khi request đã được xác thực
         * (ví dụ thông qua JWTAuthenticationFilter).
         */
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        /**
         * Nếu user đã đăng nhập và không phải anonymousUser
         * thì rate limit theo user.
         */
        if (auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal())) {
            return "rate-limit:user:" + auth.getName();
        }

        /**
         * Nếu chưa đăng nhập thì rate limit theo IP.
         */
        return "rate-limit:ip:" + request.getRemoteAddr();
    }
}
