package com.example.demo.controller;

import java.util.List;
import java.util.Scanner;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Question;

import jakarta.servlet.http.HttpSession;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Controller
public class HomeController {

    private final ChatClient chatClient;

    public HomeController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }
    
    @GetMapping("/")
    public String show(HttpSession session,ModelMap model) throws Exception {

    	 	String s1 = """
    	        Generate exactly 5 random Java True/False declarative statements.

    	        Return ONLY a valid JSON array.
    	        Do NOT return Markdown.
    	        Do NOT use ```json.
    	        Do NOT add any explanation.
    	        Do NOT add any text before or after the JSON.

    	        Every item must be a factual Java statement.
    	        Do NOT generate questions beginning with:
    	        what, why, how, which, or is.

    	        Each object MUST contain exactly these 3 fields:

    	        "qno"     : integer from 1 to 5
    	        "quesName": string containing the Java statement
    	        "ans"     : boolean true or false

    	        The response MUST contain exactly 5 objects.

    	        The response MUST start with [ and MUST end with ].

    	        Example format:

    	        [
    	          {
    	            "qno": 1,
    	            "quesName": "An interface in Java can have private methods since Java 9.",
    	            "ans": true
    	          },
    	          {
    	            "qno": 2,
    	            "quesName": "Java supports multiple inheritance through classes.",
    	            "ans": false
    	          },
    	          {
    	            "qno": 3,
    	            "quesName": "A final variable can be assigned only once.",
    	            "ans": true
    	          },
    	          {
    	            "qno": 4,
    	            "quesName": "The String class is mutable in Java.",
    	            "ans": false
    	          },
    	          {
    	            "qno": 5,
    	            "quesName": "Java supports method overloading.",
    	            "ans": true
    	          }
    	        ]
    	        """;

        String s2 = chatClient
                .prompt()
                .user(s1)
                .call()
                .content();

        ObjectMapper mapper = new ObjectMapper();
        List<Question> list = mapper.readValue(
                s2,
                new TypeReference<List<Question>>() {
                });

        session.setAttribute("list",list);
        session.setAttribute("count",0);

        model.addAttribute("current_question",list.get(0));
        
               return "question";
    }
    @GetMapping("/next")
    public String question(HttpSession session,ModelMap model,@RequestParam("userAns")boolean userans)
    {
    	List<Question> list=(List<Question>)session.getAttribute("list");
    	int count=(int)session.getAttribute("count");
    	list.get(count).setUserans(userans);
    	if(userans==list.get(count).isAns())
    	{
    		list.get(count).setMarks(1);
    	}
    	
    	if(count==4)
    	{
    		int c=0;
    		for(Question q:list)
    		{
    			c=c+q.getMarks();
    		}
    		model.addAttribute("total",c);
    		return "result";
    	}
    	session.setAttribute("count",++count);
        	model.addAttribute("current_question",list.get(count));
        	System.out.println("userans = "+userans+" correct_ans = "+list.get(count).isAns());
        	return "question";
        	    	/*if(count==list.size()-1)
    	{
    		System.out.println("userans = "+userans+" correct_ans = "+list.get(count).isAns());
    		return "result";
    	}
    	else
    	{
    		session.setAttribute("count",++count);
        	model.addAttribute("current_question",list.get(count));
        	System.out.println("userans = "+userans+" correct_ans = "+list.get(count).isAns());
        	return "question";
    	}*/
    }
}