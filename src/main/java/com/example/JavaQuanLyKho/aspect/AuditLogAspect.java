package com.example.JavaQuanLyKho.aspect;

import com.example.JavaQuanLyKho.model.entity.AuditLog;
import com.example.JavaQuanLyKho.model.entity.User;
import com.example.JavaQuanLyKho.repository.AuditLogRepository;
import com.example.JavaQuanLyKho.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Aspect
@Component
public class AuditLogAspect {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public AuditLogAspect(AuditLogRepository auditLogRepository, UserRepository userRepository,
            ObjectMapper objectMapper) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Around("execution(* com.example.JavaQuanLyKho.service.impl.*.create*(..)) || " +
            "execution(* com.example.JavaQuanLyKho.service.impl.*.save*(..)) || " +
            "execution(* com.example.JavaQuanLyKho.service.impl.*.update*(..)) || " +
            "execution(* com.example.JavaQuanLyKho.service.impl.*.delete*(..)) || " +
            "execution(* com.example.JavaQuanLyKho.service.impl.*.confirm*(..)) || " +
            "execution(* com.example.JavaQuanLyKho.service.impl.*.approve*(..)) || " +
            "execution(* com.example.JavaQuanLyKho.service.impl.*.cancel*(..)) || " +
            "execution(* com.example.JavaQuanLyKho.service.impl.*.complete*(..)) || " +
            "execution(* com.example.JavaQuanLyKho.service.impl.*.assign*(..))")
    public Object logAudit(ProceedingJoinPoint joinPoint) throws Throwable {

        // execute actual method
        Object result = null;
        Throwable error = null;

        try {
            result = joinPoint.proceed();
        } catch (Throwable t) {
            error = t;
        }

        // try saving audit
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof String) {
                String username = (String) auth.getPrincipal();
                User user = userRepository.findByUsername(username).orElse(null);
                if (user != null) {
                    AuditLog log = new AuditLog();
                    log.setActorUserId(user.getId());
                    log.setAction(joinPoint.getSignature().getName());

                    String className = joinPoint.getTarget().getClass().getSimpleName();
                    if (className.endsWith("ServiceImpl")) {
                        className = className.substring(0, className.length() - 11);
                    }
                    if (className.length() > 128)
                        className = className.substring(0, 128);
                    log.setEntityType(className);

                    // try generic get entity ID if result is an entity
                    log.setEntityId(extractEntityId(result));

                    // handle before/after JSON
                    String argsJson = safeToString(joinPoint.getArgs());
                    log.setBeforeJson(truncate(argsJson, 255)); // or more if
                                                                                                        // column
                                                                                                        // allows, wait,
                                                                                                        // database
                                                                                                        // schema
                                                                                                        // usually has
                                                                                                        // text for
                                                                                                        // json. Let's
                                                                                                        // see max
                                                                                                        // length.

                    String resultJson = error != null
                            ? "ERROR: " + error.getMessage()
                            : safeToString(result);
                    log.setAfterJson(truncate(resultJson, 255));

                    log.setCreatedAt(OffsetDateTime.now());

                    auditLogRepository.save(log);
                }
            }
        } catch (Exception e) {
            // suppress audit logging errors so it doesn't break main flow
            System.err.println("Failed to write audit log: " + e.getMessage());
        }

        if (error != null) {
            throw error;
        }
        return result;
    }

    private UUID extractEntityId(Object obj) {
        if (obj == null)
            return null;
        try {
            java.lang.reflect.Method getIdMethod = obj.getClass().getMethod("getId");
            Object idVal = getIdMethod.invoke(obj);
            if (idVal instanceof UUID) {
                return (UUID) idVal;
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private String safeToString(Object obj) {
        if (obj == null)
            return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
    }

    private String truncate(String s, int max) {
        if (s == null) return null;
        return s.length() > max ? s.substring(0, max) : s;
    }
}
