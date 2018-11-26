package com.example.springVueGrad.controllers;

import com.example.springVueGrad.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("message")
public class MessageController {
    private int counter = 4;

    private List<Map<String, String>> messages = new ArrayList<Map<String, String>>() {{
        add(new HashMap<String, String>() {{ put("id", "1"); put("text", "First message"); }});
        add(new HashMap<String, String>() {{ put("id", "2"); put("text", "Second message"); }});
        add(new HashMap<String, String>() {{ put("id", "3"); put("text", "Third message"); }});
    }};

    @GetMapping
    public List<Map<String, String>> list(){
        return messages;
    }

    @GetMapping("{id}")
    public Map<String, String> getOne(@PathVariable String id) {
        return getMessage(id);
    }
//получить сообщение по id
    private Map<String, String> getMessage(@PathVariable String id) {
        return messages.stream()
                .filter(message -> message.get("id").equals(id))//фильтруем сообщения по иквалс
                .findFirst()//берем первое попавшееся значение
                .orElseThrow(NotFoundException::new);//выкидываем 404 если не одно значение не подходит
    }
//создание сообщения
    @PostMapping
    public Map<String, String> create(@RequestBody Map<String, String> message) {//принимаем сообщение
        message.put("id", String.valueOf(counter++));//инкрементирую id чтоб он шел двльше по счету

        messages.add(message);//добавляю в список сообщений

        return message;
    }
//обновление сообщения
    @PutMapping("{id}")
    public Map<String, String> update(@PathVariable String id, @RequestBody Map<String, String> message) {
        Map<String, String> messageFromDb = getMessage(id);

        messageFromDb.putAll(message);
        messageFromDb.put("id", id);//устанавливается тот же самый id по которому был запрос, чтоб не перепутать

        return messageFromDb;
    }
//удаление сообщения
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Map<String, String> message = getMessage(id);

        messages.remove(message);
    }
}
