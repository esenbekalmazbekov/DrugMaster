package com.example.drugmaster.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.drugmaster.Model.User;
import com.example.drugmaster.Model.drugmodel.Drug;
import com.example.drugmaster.Model.ordermodel.OrderRequest;
import com.example.drugmaster.R;

import java.util.ArrayList;
import java.util.Objects;

public class RefactorActivty extends AppCompatActivity{
    private MenuItem delete;
    private OrderRequest orderRequest;
    private ArrayList<String> deletedrugs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refactor_activty);
        initialization();
    }


    private void initialization(){
        TextView userEmail = findViewById(R.id.userEmail);
        ListView orderView = findViewById(R.id.orders);
        Toolbar toolbar = findViewById(R.id.toolbar);
        deletedrugs = new ArrayList<>();
        setSupportActionBar(toolbar);


        boolean isManager = getIntent().getBooleanExtra("manager", false);
        if(isManager){
            User user = getIntent().getParcelableExtra("userdata");
            if (user != null) {
                toolbar.setTitle(user.getOrgname());
                userEmail.setText(user.getEmail());
            }
        }
        else {
            User user = getIntent().getParcelableExtra("managerdata");
            if (user != null) {
                toolbar.setTitle(user.getOrgname());
                userEmail.setText(user.getEmail());
            }
        }

        orderRequest = new OrderRequest(RefactorActivty.this, orderView);
        orderRequest.requestShortOrder();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refactor_menu,menu);
        delete = menu.findItem(R.id.delete);
        delete.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        switch (id){
            case R.id.delete:{

                for(String d : deletedrugs)
                    orderRequest.getOrder().getDrugs().remove(d);

                orderRequest.getDrugRequest().getshortlist();

                delete.setVisible(false);
                deletedrugs.clear();
            }break;
            case R.id.save:{
                newCost();
            }break;
            case R.id.close:{
                finish();
            }break;
        }

        return true;
    }
    private void newCost(){
        double d = 0.0;
        for (Drug drug : orderRequest.getDrugRequest().getDrugarray()){
            d = d + Objects.requireNonNull(orderRequest.getOrder().getDrugs().get(drug.getId()))* Double.parseDouble(drug.getPrice());
        }
        d = d * 100;
        int i = (int)d;
        d =  (double)i / 100;
        orderRequest.getOrder().setCost(d);
        orderRequest.changeSimpleOrder();
    }
    public ArrayList<String> getDeletedrugs() {
        return deletedrugs;
    }

    public void deleteButtonShow(){
        if(deletedrugs.size() == 0){
            delete.setVisible(false);
        }else{
            delete.setVisible(true);
        }
    }
}