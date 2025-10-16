package dev.funa.headsChat;

import java.util.ArrayList;
import java.util.List;

public class Sorts {
    public static List<Integer> selectionSort(List<Integer> list) {
        List<Integer> sortedList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).compareTo(list.get(j)) > 0) {
                    Integer temp = list.get(i);
                    sortedList.set(i, list.get(j));
                    sortedList.set(j, temp);
                }
            }
        }
        return sortedList;
    }

    public static List<Integer> insertionSort(List<Integer> list) {
        List<Integer> sortedList = new ArrayList<>();
        sortedList.addAll(list);
        for (int i = 1; i < list.size(); i++) {
            Integer temp = list.get(i);
            Integer index = i - 1;
            while (index > 0 && list.get(index) > temp) {
                list.set(index + 1, list.get(index));
                index--;
            }
            list.set(index + 1, temp);
        }
        return sortedList;
    }
}
