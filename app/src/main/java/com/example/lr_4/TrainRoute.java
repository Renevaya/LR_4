package com.example.lr_4;
//для представления информации о маршруте поезда, включая его идентификатор, номер маршрута, дату отправления и дату прибытия.
public class TrainRoute {
    public int _id;//для хранения идентификатора маршрута поезда.
    public String route;//для хранения номера маршрута поезда.
    public String departureDate;//для хранения даты отправления поезда.
    public String arrivalDate;//для хранения даты прибытия поезда.
    //Конструктор для создания объекта
    public TrainRoute(int id, String rt, String depDate, String arrDate){
        _id = id;
        route = rt;
        departureDate = depDate;
        arrivalDate = arrDate;
    }
}
