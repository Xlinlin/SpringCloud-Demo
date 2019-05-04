package com.skywalking.mqtt;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/5/4 14:32
 * @since JDK 1.8
 */
@Controller()
public class MvcController
{
    @RequestMapping("/mqtt")
    public ModelAndView mqtt(){
        ModelAndView mv = new ModelAndView();
//        mv.addObject("msg", "this a msg from HelloWorldController");
        mv.setViewName("mqtt_client");;
        return mv;
    }
}
