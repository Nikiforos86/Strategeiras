package gr.stratego.patrastournament.me.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtils {

    /**
     * Returns if the provided collection is empty or not.
     *
     * @param collection the collection
     * @return true, if empty
     */
    public static boolean isEmpty(final Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }

    /**
     * Returns if the provided collection is empty or not.
     *
     * @param collection the collection
     * @return true, if not empty
     */
    public static boolean isNotEmpty(final Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * Returns if the provided position is in the boundaries of the given Collection
     *
     * @param collection the collection
     * @param position   the position
     * @return true, if position is valid
     */
    public static boolean isValidPosition(final Collection<?> collection, int position) {
        if (isEmpty(collection)) {
            return false;
        }
        if (position >= 0 && position < collection.size()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns if the provided position is in the boundaries of the given Collection
     *
     * @param collection the collection
     * @param position   the position
     * @param plusSize   it is used in case we want to calculate some extra
     * @return true, if position is valid
     */
    public static boolean isValidPosition(final Collection<?> collection, int position, int plusSize) {
        if (isEmpty(collection)) {
            return false;
        }
        if (position >= 0 && position - plusSize < collection.size()) {
            return true;
        } else {
            return false;
        }
    }

    public interface Predicate<T> {
        boolean apply(T type);
    }

    public static <T> Collection<T> filter(Collection<T> col, Predicate<T> predicate) {
        Collection<T> result = new ArrayList<T>();
        for (T element : col) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }
        return result;
    }

//    /**
//     * Returns if the provided collections are equal or not.
//     * It compares efficiently every list object
//     *
//     * @param list1 is a sorted collection
//     * @param list2 is a sorted collection
//     * @return true, if collections are equal
//     */
//    public static <T> boolean areEqual(List<T> list1, List<T> list2) {
//        try {
//            if (isEmpty(list1) || isEmpty(list2) || list1.size() != list2.size()) {
//                return false;
//            }
//            int pace = 50;
//            if (list1.size() < pace) {
//                pace = list1.size();
//            }
//            String list1String = "";
//            String list2String = "";
//            for (int i = 0; i < list1.size(); i += pace) {
//                int limit = i + pace;
//                if (limit > list1.size()) {
//                    limit = list1.size();
//                }
//                list1String = JsonUtils.convertJsonObjectToString(list1.subList(i, limit));
//                list2String = JsonUtils.convertJsonObjectToString(list2.subList(i, limit));
//                if (!StringUtils.areEqual(list1String, list2String)) {
//                    return false;
//                }
//            }
//            return true;
//        } catch (Exception ex) {
//            return false;
//        }
//    }
}