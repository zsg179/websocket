package com.tk.sz.model;

public class VoteBaseVo {
    private Integer id;

    private String votecode;

    private String votename;

    private Integer firstprize;

    private Integer secondprize;

    private Integer thirdprize;

    private Integer fourprize;

    private Integer fifthprize;

    private Integer totalprize;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVotecode() {
        return votecode;
    }

    public void setVotecode(String votecode) {
        this.votecode = votecode == null ? null : votecode.trim();
    }

    public String getVotename() {
        return votename;
    }

    public void setVotename(String votename) {
        this.votename = votename == null ? null : votename.trim();
    }

    public Integer getFirstprize() {
        return firstprize;
    }

    public void setFirstprize(Integer firstprize) {
        this.firstprize = firstprize;
    }

    public Integer getSecondprize() {
        return secondprize;
    }

    public void setSecondprize(Integer secondprize) {
        this.secondprize = secondprize;
    }

    public Integer getThirdprize() {
        return thirdprize;
    }

    public void setThirdprize(Integer thirdprize) {
        this.thirdprize = thirdprize;
    }

    public Integer getFourprize() {
        return fourprize;
    }

    public void setFourprize(Integer fourprize) {
        this.fourprize = fourprize;
    }

    public Integer getFifthprize() {
        return fifthprize;
    }

    public void setFifthprize(Integer fifthprize) {
        this.fifthprize = fifthprize;
    }

    public Integer getTotalprize() {
        return totalprize;
    }

    public void setTotalprize(Integer totalprize) {
        this.totalprize = totalprize;
    }
}