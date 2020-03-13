package com.example.meufortinite.MODEL.API.DTO;

public class PlayerStatsDetail {
    private FinalStats matches;
    private FinalStats kd;
    private FinalStats score;
    private FinalStats trnRating;

    public FinalStats getMatches() {
        return matches;
    }

    public void setMatches(FinalStats matches) {
        this.matches = matches;
    }

    public FinalStats getKd() {
        return kd;
    }

    public void setKd(FinalStats kda) {
        this.kd = kda;
    }

    public FinalStats getScore() {
        return score;
    }

    public void setScore(FinalStats score) {
        this.score = score;
    }

    public FinalStats getTrnRating() {
        return trnRating;
    }

    public void setTrnRating(FinalStats trnRating) {
        this.trnRating = trnRating;
    }
}
