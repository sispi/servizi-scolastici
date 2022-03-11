package keysuite.docer.controllers;

import it.kdm.orchestratore.session.Session;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("model")
public class ModelController {
    @RequestMapping("userinfo")
    public Object userinfo(HttpServletRequest req) throws Exception {
        return Session.getUserInfoNoExc();
    }
}
