package pl.wojciechgunia.wgapi.fasade;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/mailing")
@RequiredArgsConstructor
public class MailingController {
}
