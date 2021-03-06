package com.game.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.Date;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private PlayerService service;

    @GetMapping(value = "/rest/players")
    public @ResponseBody List<Player> getSortedPlayers(@RequestParam(value="name", required = false) String name,
                     @RequestParam(value = "title", required = false) String title,
                     @RequestParam(value = "race", required = false) Race race,
                     @RequestParam(value = "profession", required = false) Profession profession,
                     @RequestParam(value = "after", required = false) Long after,
                     @RequestParam(value = "before", required = false) Long before,
                     @RequestParam(value = "banned", required = false) String banned,
                     @RequestParam(value = "minExperience", required = false) Integer minExperience,
                     @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                     @RequestParam(value = "minLevel", required = false) Integer minLevel,
                     @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                     @RequestParam(defaultValue = "0", value="pageNumber")String page,
                     @RequestParam(defaultValue = "3", value = "pageSize")String pageSize,
                     @RequestParam(defaultValue = "ID", value = "order")String order){
        List<Player> resultList = service.getFilteredPlayers(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
        List<Player> playerList = service.getSortedPlayers(resultList, page, pageSize, order);
        return playerList;
    }
    @PostMapping(value = "/rest/players")
    public @ResponseBody Player createPlayer(@RequestBody Player player){
        player = service.save(player);
        return player;
    }
    @PostMapping(value = "/rest/players/{id}")
    public @ResponseBody Player updatePlayer(
            @PathVariable (value = "id", required = false) Long id,
            @RequestBody (required = false) Player player){
        if (player.getName() == null && player.getTitle() == null && player.getProfession() == null && player.getRace() == null && player.getBirthday() == null && player.getExperience() == null)
            return service.getPlayerById(id);
        Player resultPlayer = service.update(player, id);
        return resultPlayer;
    }


    @RequestMapping(value = "/rest/players/count", method = RequestMethod.GET)
    public @ResponseBody int getCount(@RequestParam(value="name", required = false) String name,
                                      @RequestParam(value = "title", required = false) String title,
                                      @RequestParam(value = "race", required = false) Race race,
                                      @RequestParam(value = "profession", required = false) Profession profession,
                                      @RequestParam(value = "after", required = false) Long after,
                                      @RequestParam(value = "before", required = false) Long before,
                                      @RequestParam(value = "banned", required = false) String banned,
                                      @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                      @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                      @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                      @RequestParam(value = "maxLevel", required = false) Integer maxLevel){
        List<Player> playerList = service.getFilteredPlayers(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
        return playerList.size();
    }
    @GetMapping(value = "rest/players/{id}")
    public @ResponseBody Player getPlayer(@PathVariable ("id") long id){
            return service.getPlayerById(id);
    }

    @DeleteMapping(value = "rest/players/{id}")
    public void deletePlayer(@PathVariable("id") long id){
        Player player = service.getPlayerById(id);
        service.delete(player);
        throw new ResponseStatusException(HttpStatus.OK);
    }

}
