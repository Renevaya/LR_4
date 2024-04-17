package com.example.lr_4;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
//private — имеют доступ только методы самого объекта.
//protected — имеют доступ методы объекта и его потомков.
//public — кто угодно.
public class MainActivity extends AppCompatActivity{

    SQLHelper database; //переменная для работы с базой данных.
    LinearLayout listLinearLayout; //переменная для линейного контейнера, который будет содержать список записей о маршрутах
    LinearLayout row; //переменная для создания горизонтальных линейных контейнеров, представляющих каждую запись о маршруте

    Dialog addNewRouteDialog;
    Dialog deleteRouteDialog;
    Dialog updateRouteDialog;
    Dialog showRouteByIDDialog;

    ArrayList<TrainRoute> trainRoutes;
    //@Override: Аннотация, которая указывает, что метод переопределяет метод базового класса (AppCompatActivity)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//вызывает конструктор родительского класса AppCompatActivity
        setContentView(R.layout.activity_main);
        database = new SQLHelper(this);

        listLinearLayout = findViewById(R.id.list);//для поиска элемента интерфейса по его идентификатору

        addNewRouteDialog = new Dialog(this);
        addNewRouteDialog.setContentView(R.layout.add_new_route_dialog);

        showRouteByIDDialog = new Dialog(this);
        showRouteByIDDialog.setContentView(R.layout.show_by_id_dialog);

        updateRouteDialog = new Dialog(this);
        updateRouteDialog.setContentView(R.layout.update_route_dialog);

        deleteRouteDialog = new Dialog(this);
        deleteRouteDialog.setContentView(R.layout.delete_route_dialog);
    }

    //метод для вывода записи по id
    public void showTrainRoute(int id){

        TrainRoute t = trainRoutes.get(id-1); //получает объект класса TrainRoute из списка trainRoutes по индексу

        row = new LinearLayout(this); //создает новый горизонтальный линейный контейнер
        row.setOrientation(LinearLayout.HORIZONTAL); //устанавливает ориентацию контейнера row как горизонтальную

        TextView Id = new TextView(this); //для отображения идентификатора маршрута.
        Id.setText( Integer.toString(t._id));
        Id.setWidth(120);

        TextView route = new TextView(this); //для отображения названия маршрута.
        route.setText(t.route);
        route.setWidth(500);

        TextView departureDate = new TextView(this); //для отображения даты отправления.
        departureDate.setText(t.departureDate);
        departureDate.setWidth(120);

        TextView arrivalDate = new TextView(this); //для отображения даты прибытия.
        arrivalDate.setText(t.arrivalDate);
        arrivalDate.setWidth(120);

        row.addView(Id);
        row.addView(route);
        row.addView(departureDate);
        row.addView(arrivalDate);

        listLinearLayout.addView(row);
    }
    //обработчик для вывода всех записей на экран
    public void showAllRoutes(View view){
        trainRoutes = getList();
        listLinearLayout.removeAllViews();
        for(int i = 1; i < trainRoutes.size()+1; i++) {
            showTrainRoute(i);
        }
    }
    //обработчик для открытия диалогового окна добавления записи
    public void onClickAddButton(View v){
        addNewRouteDialog.show();
    }

    public void onClickAddConfirmationButton(View v){
        //Получение данных из полей ввода
        EditText routeNameEditText = addNewRouteDialog.findViewById(R.id.route_name_edittext);
        EditText departureDateEditText = addNewRouteDialog.findViewById(R.id.departure_date_edittext);
        EditText arrivalDateEditText = addNewRouteDialog.findViewById(R.id.arrival_date_edittext);
        //Извлечение информации
        String routeName = routeNameEditText.getText().toString();
        String departureDate = departureDateEditText.getText().toString();
        String arrivalDate = arrivalDateEditText.getText().toString();
        //Добавление маршрута в базу данных
        database.addTrainRoute(routeName, departureDate, arrivalDate);
        //Очистка полей ввода
        routeNameEditText.setText("");
        departureDateEditText.setText("");
        arrivalDateEditText.setText("");

        showAllRoutes(v);
    }
    //обработчик для открытия диалогового окна изменения записи
    public void onClickEditButton(View v){
        updateRouteDialog.show();
    }

    public void onClickEditConfirmationButton(View v){
        //Получение данных из полей ввода
        EditText idEditText = updateRouteDialog.findViewById(R.id.route_id_edittext);
        EditText routeNameEditText = updateRouteDialog.findViewById(R.id.route_name_edittext);
        EditText departureDateEditText = updateRouteDialog.findViewById(R.id.departure_date_edittext);
        EditText arrivalDateEditText = updateRouteDialog.findViewById(R.id.arrival_date_edittext);
        //Извлечение информации
        int id = Integer.parseInt(idEditText.getText().toString());
        String routeName = routeNameEditText.getText().toString();
        String departureDate = departureDateEditText.getText().toString();
        String arrivalDate = arrivalDateEditText.getText().toString();
        //Обновление маршрута в базе данных
        database.updateRoute(id ,routeName, departureDate, arrivalDate);
        //Очистка полей ввода
        idEditText.setText("");
        routeNameEditText.setText("");
        departureDateEditText.setText("");
        arrivalDateEditText.setText("");

        showAllRoutes(v);
    }
    //обработчик для открытия диалогового окна вывода записи на экран по указанному ID
    public void onClickShowByIDButton(View v){
        showRouteByIDDialog.show();
    }

    public void onClickShowByIDApplyButton(View v){
        EditText editText = showRouteByIDDialog.findViewById(R.id.help);//Эта строка находит поле ввода текста (EditText)
        int id = Integer.parseInt(editText.getText().toString());//извлекается текст, введенный пользователем в поле editText
        listLinearLayout.removeAllViews();
        showTrainRoute(id);
        editText.setText("");
    }

    //обработчик для открытия диалогового окна удаления записи по указанному ID
    public void onClickDeleteRouteButton(View v){
        deleteRouteDialog.show();
    }

    public void onClickDeleteRouteApplyButton(View v){
        EditText editText = deleteRouteDialog.findViewById(R.id.route_id_edittext);//находит поле ввода текста (EditText)
        int id = Integer.parseInt(editText.getText().toString());//Здесь извлекается текст, введенный пользователем в поле editText
        database.deleteRoute(id);
        editText.setText("");
        showAllRoutes(v);
    }

    public ArrayList<TrainRoute> getList(){
        ArrayList<TrainRoute> trainRoutes = new ArrayList<>();//Создается пустой список объектов типа TrainRoute
        Cursor cursor = database.getFullTable();//Получение данных из базы данных
        if (cursor != null){
            while (cursor.moveToNext()){//Перебор данных по строкам
                trainRoutes.add(
                        new TrainRoute(
                                cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3)));
            }
        }
        return trainRoutes;
    }
}