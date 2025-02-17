//package org.example.digitaldrawer.buttons;
//
//import org.example.exceptions.MissingButtonException;
//
//import java.util.HashMap;
//
///**
// * Класс для создания кнопок на панелях
// */
//public class ButtonFactory {
//
//    private HashMap<String, Object> buttons;
//
//    /**
//     * Конструктор класса
//     **/
//    public ButtonFactory(){
//        buttons = new HashMap<>();
//    }
//
//    /**
//     * Метод, при помощи которого можно создать кнопку из списка существующих
//     * @param name - название типа кнопки
//     * @return возвращает готовую кнопку или выбрасывает исключение, если названия
//     * кнопки не существует
//     */
//    public Object createButton(String name){
//        Object buttonBaseSupplier = buttons.get(name);
//        if(buttonBaseSupplier != null){
////           logger.log(System.Logger.Level.ERROR, "Button with this name doesn`t exist!");
//            return buttonBaseSupplier;
//        }
//        throw new MissingButtonException("Button with this name doesn`t exist!");
//    }
//
//    /**
//     * Метод, предназначенный для добавления нового типа кнопки в список уже
//     * существующих кнопок
//     * @param buttonName - название кнопки
//     * @param button - кнопка, которая добавляется в список уже существующих кнопок
//     */
//    public void updateButtonsList(String buttonName, Object button) {
//        buttons.put(buttonName, button);
//    }
//
//    /**
//     * Геттер
//     * @return возвращает мапу кнопок
//     */
//    public HashMap<String, Object> getButtons() {
//        return buttons;
//    }
//}
