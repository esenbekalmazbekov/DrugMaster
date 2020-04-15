package com.example.drugmaster.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.drugmaster.Model.User;
import com.example.drugmaster.Model.ordermodel.Order;
import com.example.drugmaster.Model.ordermodel.OrderRequest;
import com.example.drugmaster.R;

import java.util.ArrayList;

public class RefactorActivty extends AppCompatActivity{
    private MenuItem delete,save,close;
    private ListView orderView;

    private Order myorder;
    private User manager;
    private OrderRequest orderRequest;
    private ArrayList<String> deletedrugs;
    private boolean change = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refactor_activty);
        initialization();

    }


    private void initialization(){
        TextView userEmail = findViewById(R.id.userEmail);
        orderView = findViewById(R.id.orders);
        Toolbar toolbar = findViewById(R.id.toolbar);
        deletedrugs = new ArrayList<>();
        setSupportActionBar(toolbar);

        manager = getIntent().getParcelableExtra("managerdata");

        if (manager != null) {
            toolbar.setTitle(manager.getOrgname());
            userEmail.setText(manager.getEmail());
        }

        orderRequest = new OrderRequest(RefactorActivty.this,orderView);
        orderRequest.requestShortOrder();

    }

    public void createOrderCopy(Order order){
        myorder = new Order(manager.getId());
        myorder.setCost(order.getCost());
        myorder.setStatus(order.getStatus());
        myorder.getDrugs().putAll(order.getDrugs());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refactor_menu,menu);
        delete = menu.findItem(R.id.delete);
        save = menu.findItem(R.id.save);
        delete.setVisible(false);
        save.setVisible(false);
        close = menu.findItem(R.id.close);
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
                save.setVisible(true);
                change = true;
            }break;
            case R.id.save:{
                Toast.makeText(this,"save",Toast.LENGTH_LONG).show();
            }break;
            case R.id.close:{
                finish();
            }break;
        }

        return true;
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