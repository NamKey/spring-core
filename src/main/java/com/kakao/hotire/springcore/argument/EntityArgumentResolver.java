package com.kakao.hotire.springcore.argument;

import com.kakao.hotire.springcore.argument.entity.Entity;
import com.kakao.hotire.springcore.argument.entity.Kakao;
import com.kakao.hotire.springcore.argument.entity.Line;
import com.kakao.hotire.springcore.argument.service.KakaoService;
import com.kakao.hotire.springcore.argument.service.LineService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static com.kakao.hotire.springcore.argument.EntityArgumentResolver.EntityArgumentType.KAKAO;
import static com.kakao.hotire.springcore.argument.EntityArgumentResolver.EntityArgumentType.LINE;
import static org.springframework.web.bind.annotation.ValueConstants.DEFAULT_NONE;

public class EntityArgumentResolver implements HandlerMethodArgumentResolver, ApplicationContextAware {

    public enum EntityArgumentType {
        KAKAO("kakaoId"),
        LINE("lineId");
        private final String path;
        @Getter(AccessLevel.PRIVATE)
        @Setter(AccessLevel.PRIVATE)
        private Map<Class<?>, Function<Long, Entity>> typeHandler;

        EntityArgumentType(final String path) {
            this.path = path;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        final KakaoService kakaoService = applicationContext.getBean(KakaoService.class);
        final LineService lineService = applicationContext.getBean(LineService.class);

        KAKAO.typeHandler = Map.of(Kakao.class, kakaoService::findById);
        LINE.typeHandler = Map.of(Line.class, lineService::findById);
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasMethodAnnotation(EntityArgument.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        @SuppressWarnings("unchecked")
        final Map<String, String> pathVariables = (Map<String, String>) Objects.requireNonNull(nativeWebRequest.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST));

        final Class<?> entityType = methodParameter.getParameter().getType();
        final EntityArgument entityArgument = Objects.requireNonNull(methodParameter.getMethodAnnotation(EntityArgument.class));
        final EntityArgumentType entityArgumentType = entityArgument.type();
        final String value = DEFAULT_NONE.equals(entityArgument.value()) ? entityArgumentType.path : entityArgument.value();

        return Optional.ofNullable(entityArgumentType.getTypeHandler().get(entityType))
                .map(handler -> handler.apply(Long.valueOf(pathVariables.get(value))))
                .orElseThrow(() -> new IllegalArgumentException("Cannot support entity type" + entityType));
    }
}
