package com.example.Controller.Exception;

import com.example.Entity.RestBeanNew;
import com.example.Util.MyException;
import jakarta.servlet.ServletException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
        @ExceptionHandler({Throwable.class, MyException.class})         //需要捕获的异常
        public RestBeanNew<Void> Exception(Exception exception){
            if(exception instanceof NoHandlerFoundException noHandlerFoundException) //404异常处理
                return RestBeanNew.failure(404,noHandlerFoundException.getMessage());
            else if (exception instanceof ServletException servletException)      //其他Servlet异常抛400，也可以更细致
                return RestBeanNew.failure(400,servletException.getMessage());
            else if(exception instanceof MailSendException mailSendException)
                return RestBeanNew.failure(406,mailSendException.getMessage());
            else if (exception instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
                log.warn("Resolve [{}:{}]",methodArgumentNotValidException.getClass().getName(), Objects.requireNonNull(methodArgumentNotValidException.getBindingResult().getFieldError()).getDefaultMessage());
                return RestBeanNew.failure(407, Objects.requireNonNull(methodArgumentNotValidException.getBindingResult().getFieldError()).getDefaultMessage());
            }
            else if (exception instanceof MyException myException){
                return RestBeanNew.failure(408,myException.getMessage());
            }
            else //其余抛500
            {
                log.error(String.valueOf(exception));
                return RestBeanNew.failure(500, exception.getMessage());
            }
        }
}
