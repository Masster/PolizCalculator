package com.masstersoft.checktv_2_0_2.app;

import java.util.LinkedList;
import com.masstersoft.checktv_2_0_2.app.KeyNames.*;

/**
 * Created by WismutPC on 03.04.15.
 */
public class AdapterForKeyboard {

    private String sUserParseInput;

    private Double dResult;
    private boolean isDotPressed;

    private LinkedList<String> allUser;
    private LinkedList<String> outVector;
    private LinkedList<String> opVector;

    private LinkedList<String> resultVector;

    private String tmpOUT;
    private Boolean numNEW;

    private Keys lastKey;

    public AdapterForKeyboard() {
        sUserParseInput = "";

        isDotPressed = false;
        allUser = new LinkedList<String>();

        outVector = new LinkedList<String>();
        opVector = new LinkedList<String>();
        resultVector = new LinkedList<String>();

        tmpOUT = "";
        numNEW = false;

        lastKey = Keys.NONE;
    }

    // Вспомогательные функции для построения и расчета ПОЛИЗ
    private boolean isNumber(String str) {
        Double d;
        try {
            d = Double.parseDouble(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean isOperation(String str) {
        if (str.equals("-") || str.equals("+") || str.equals("*") || str.equals("/")) {
            return true;
        } else {
            return false;
        }
    }

    private int getPrior(String str) {
        int pr = -1;
        if (str.equals("-") || str.equals("+")) {
            pr = 1;

        } else if (str.equals("*") || str.equals("/")) {
            pr = 2;

        }
        return pr;
    }

    private boolean isPriorAll(String key) {
        boolean isP = true;
        for (String k : opVector) {
            if (getPrior(key) < getPrior(k)) {
                isP = false;
            }
        }
        return isP;
    }

    // Алгоритм построения ПОЛИЗ по стеку расчета
    private void BuildPoliz() {
        opVector.clear();
        outVector.clear();
        for (String item : allUser) {
            if (isNumber(item)) {
                outVector.add(item);
            }
            if (isOperation(item)) {
                if ((opVector.size() == 0) || isPriorAll(item)) {
                    opVector.add(item);
                } else {
                    while (opVector.size() > 0 && getPrior(opVector.peekLast()) >= getPrior(item)) {
                        outVector.add(opVector.pollLast());
                    }
                    if ((opVector.size() == 0) || isPriorAll(item)) {
                        opVector.add(item);
                    }
                }
            }
        }
        while (opVector.size() > 0) {
            outVector.add(opVector.pollLast());
        }
    }

    // Стандартный прямой алгоритм расчета ПОЛИЗ
    private void EvaluatePoliz() {
        if (outVector.size() > 1) {
            for (String item : outVector) {
                if (isNumber(item)) {
                    resultVector.add(item);
                }
                if (isOperation(item)) {
                    Double right = Double.parseDouble(resultVector.pollLast());
                    Double left = Double.parseDouble(resultVector.pollLast());
                    Double res = 0d;

                    if (item.equals("+")) {
                        res = left + right;
                    }
                    if (item.equals("-")) {
                        res = left - right;
                    }
                    if (item.equals("*")) {
                        res = left * right;
                    }
                    if (item.equals("/")) {
                        res = left / right;
                    }
                    resultVector.add(String.valueOf(res));
                }
            }
        }
        System.out.println(resultVector);
        if (resultVector.size() > 0) {
            dResult = Double.parseDouble(resultVector.poll());

        } else {
            dResult = 0d;
        }
    }

    // Процедура нажатия кнопки Clear
    private void ClearButtonPressed(Keys key) {
        if (key == Keys.CLEAR) {
            allUser.clear();
            opVector.clear();
            outVector.clear();
            resultVector.clear();
            tmpOUT = "";
            isDotPressed = false;
            numNEW = false;
        }
    }

    // Получаю очередное число для добавения его в выражение для расчета.
    private void BuildNumber(Keys key) {
        if(lastKey == Keys.EQUAL) if (tmpOUT.indexOf(".")>0) isDotPressed=true;
        if ((KeyNames.isNumber(key)) || (KeyNames.isDot(key))) {
            if ((KeyNames.isDot(key)) && (!isDotPressed)) {
                if (tmpOUT.length() == 0) {
                    tmpOUT += "0";
                }
                tmpOUT += KeysToString(key);
                isDotPressed = true;
            }
            if (KeyNames.isNumber(key)) {
                numNEW = true;
                tmpOUT += KeysToString(key);
            }
        }
    }

    // Обработка нажатия кнопки арифметической операции
    private void OperationButtonPressed(Keys key) {
        if (KeyNames.isOperation(key)) {
            if (numNEW) {
                allUser.add(tmpOUT);
                numNEW = false;
                isDotPressed = false;
                tmpOUT = "";
            }

            if (allUser.size() >= 1) {
                if (isOperation(allUser.getLast())) {
                    allUser.pollLast();
                    allUser.add(KeysToString(key));
                } else {
                    allUser.add(KeysToString(key));
                }
            }
        }
    }

    private void TrashOutput(Keys key) {
        FromListToParse();
        System.out.println("allUser "+allUser);
        System.out.println("evaluation "+sUserParseInput);
    }

// Обработка нажатия на кнопку "равно"
    private void EqualKeyPressed(Keys key) {
        if (key == Keys.EQUAL) {

            // Если последний объект - число, заношу его в выражение для расчета,
            // иначе просто удаляю последний элемент из выражения, т.к. он будет операцией            
            if (isNumber(tmpOUT)) {
                allUser.add(tmpOUT);
            } else {
                allUser.pollLast();
            }

            // Получаю выражение в виде ПОЛИЗ
            BuildPoliz();
            // Вычисляю ПОЛИЗ и кладу ответ в dResult
            EvaluatePoliz();
            // Имитирую нажатие кнопки очистки экрана    
            ClearButtonPressed(Keys.CLEAR);
            
            tmpOUT=String.valueOf(dResult);
            numNEW = true;
        }
    }
// Получаю правильное выражение для расчета из стека allUser
    private void FromListToParse() {
        sUserParseInput = "";
        for (String s : allUser) {
            sUserParseInput += s;
        }
        sUserParseInput += tmpOUT;
    }
// Перевод из кодового обозначения нажатой кнопки в строковое его представление
    private String KeysToString(Keys key) {
        String outTMP;
        switch (key) {
            case ONE:
                outTMP = "1";
                break;
            case TWO:
                outTMP = "2";
                break;
            case THREE:
                outTMP = "3";
                break;
            case FOUR:
                outTMP = "4";
                break;
            case FIVE:
                outTMP = "5";
                break;
            case SIX:
                outTMP = "6";
                break;
            case SEVEN:
                outTMP = "7";
                break;
            case EIGHT:
                outTMP = "8";
                break;
            case NINE:
                outTMP = "9";
                break;
            case ZERO:
                outTMP = "0";
                break;
            case DOT:
                outTMP = ".";
                break;
            case MINUS:
                outTMP = "-";
                break;
            case PLUS:
                outTMP = "+";
                break;
            case MULTIPLY:
                outTMP = "*";
                break;
            case DIVISION:
                outTMP = "/";
                break;
            default:
                outTMP = "";
        }
        return outTMP;
    }
    
    public Double Result() {
        return dResult;
    }   

    public String getEvaluation() {
        return sUserParseInput;
    }
  
    // Обрабатываю нажатия кнопок клавиатуры
    public void OnKeyPressed(Keys key) {
        ClearButtonPressed(key);

        BuildNumber(key);

        OperationButtonPressed(key);

        TrashOutput(key);

        EqualKeyPressed(key);

        lastKey = key;
    }
    
}
