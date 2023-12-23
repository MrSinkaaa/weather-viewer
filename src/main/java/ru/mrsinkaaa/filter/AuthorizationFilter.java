package ru.mrsinkaaa.filter;

import ru.mrsinkaaa.dto.UserDTO;
import ru.mrsinkaaa.utils.PathUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class AuthorizationFilter implements Filter {

    private static final Set<String> PUBLIC_PATH = Set.of(PathUtil.LOGIN, PathUtil.REGISTRATION, PathUtil.IMAGES, PathUtil.STYLES);


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestURI = ((HttpServletRequest) request).getRequestURI();

        if(isPublicPath(requestURI) || isUserAuthorized(request)) {
            chain.doFilter(request, response);
        } else {
            reject(request, response);
        }
    }

    private boolean isPublicPath(String requestURI) {
        return PUBLIC_PATH.stream().anyMatch(requestURI::startsWith);
    }

    private boolean isUserAuthorized(ServletRequest request) {
        var user = (UserDTO) ((HttpServletRequest) request).getSession().getAttribute("user");
        return user != null;
    }

    private void reject(ServletRequest request, ServletResponse response) throws IOException {
        String prevPage = ((HttpServletRequest) request).getHeader("referer");
        ((HttpServletResponse) response).sendRedirect(prevPage != null ? prevPage : PathUtil.LOGIN);
    }
}
