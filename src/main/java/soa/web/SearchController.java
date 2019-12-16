package soa.web;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@Controller
public class SearchController {

  private final ProducerTemplate producerTemplate;

  @Autowired
  public SearchController(ProducerTemplate producerTemplate) {
    this.producerTemplate = producerTemplate;
  }

  @RequestMapping("/")
  public String index() {
    return "index";
  }


  @RequestMapping(value = "/search")
  @ResponseBody
  public Object search(@RequestParam("q") String q,
                       @RequestParam(value = "max", required = false, defaultValue = "10") int max) {

    // Parse the q if contains <word> max:<Number>
    if(q.contains("max:")){
      // Obtain the number between the parameter max
      String patternMax = "max:";
      String maxString = q.substring(q.indexOf("max") + patternMax.length());
      max = Integer.parseInt(maxString);
      // Delete the max:X from the query
      q = q.substring(0, q.indexOf("max") - 1);
    }

    System.out.println("Tweets buscados por <" + q +">, mostrando " + max + " resultados");

    Map<String, Object> headers = new HashMap<>();
    headers.put("CamelTwitterKeywords", q);
    headers.put("CamelTwitterCount", max);
    return producerTemplate.requestBodyAndHeaders("direct:search","", headers);


  }
}