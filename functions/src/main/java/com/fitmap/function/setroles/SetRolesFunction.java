package com.fitmap.function.setroles;

import javax.validation.ConstraintViolationException;

import com.fitmap.function.common.config.SystemTimeZoneConfig;
import com.fitmap.function.common.exception.TerminalException;
import com.fitmap.function.common.service.CheckConstraintsRequestBodyService;
import com.fitmap.function.common.service.CheckRequestContentTypeService;
import com.fitmap.function.common.service.CheckRequestMethodService;
import com.fitmap.function.common.service.ReadRequestBodyService;
import com.fitmap.function.common.service.ResponseService;
import com.fitmap.function.setroles.payload.request.SetRolesRequest;
import com.fitmap.function.setroles.service.SetRolesService;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

public class SetRolesFunction implements HttpFunction {

    static {

        SystemTimeZoneConfig.setUtcDefaultTimeZone();
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {

        try {

            CheckRequestMethodService.checkPostMethod(request);

            CheckRequestContentTypeService.checkApplicationJsonContentType(request);

            var body = ReadRequestBodyService.getBody(request, SetRolesRequest.class);

            CheckConstraintsRequestBodyService.checkConstraints(body);

            SetRolesService.setRoles(body);

        } catch (TerminalException e) { ResponseService.answerTerminalException(request, response, e); }
          catch (MethodNotAllowedException e) { ResponseService.answerMethodNotAllowed(request, response, e); }
          catch (UnsupportedMediaTypeStatusException e) { ResponseService.answerBadRequest(request, response, e); }
          catch (HttpMessageNotReadableException e) { ResponseService.answerBadRequest(request, response, e); }
          catch (ConstraintViolationException e) { ResponseService.answerBadRequest(request, response, e); }
          catch (Exception e) { ResponseService.answerInternalServerError(request, response, e); }

    }

}
