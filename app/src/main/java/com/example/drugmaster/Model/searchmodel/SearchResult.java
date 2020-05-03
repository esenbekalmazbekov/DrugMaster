package com.example.drugmaster.Model.searchmodel;

import com.example.drugmaster.Model.User;
import com.example.drugmaster.Model.drugmodel.Drug;

class SearchResult {
    private User managerID;
    private Drug drug;

    SearchResult(User managerID, Drug drug) {
        this.managerID = managerID;
        this.drug = drug;
    }

    User getManagerID() {
        return managerID;
    }

    Drug getDrug() {
        return drug;
    }
}
