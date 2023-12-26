package com.tejko.yamb.models.payload.dto;

import java.util.List;

import com.tejko.yamb.models.enums.BoxType;
import com.tejko.yamb.models.enums.GameStatus;

public class GameDTO {

    private Long id;
    private String player;
    private SheetDTO sheet;
    private List<DiceDTO> dices;
    private int rollCount;
    private BoxType announcement;
    private GameStatus status;

    public GameDTO(Long id, String player, SheetDTO sheet, List<DiceDTO> dices, int rollCount, BoxType announcement, GameStatus status) {
        this.id = id;
        this.player = player;
        this.sheet = sheet;
        this.dices = dices;
        this.rollCount = rollCount;
        this.announcement = announcement;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getPlayer() {
        return player;
    }

    public SheetDTO getSheet() {
        return sheet;
    }

    public List<DiceDTO> getDices() {
        return dices;
    }

    public int getRollCount() {
        return rollCount;
    }

    public BoxType getAnnouncement() {
        return announcement;
    }

    public GameStatus getStatus() {
        return status;
    }

}
