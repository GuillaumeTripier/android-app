package esgi.meteoapp.city;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<CityItem> ITEMS = new ArrayList<CityItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, CityItem> ITEM_MAP = new HashMap<String, CityItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createCityItem(i));
        }
    }

    private static void addItem(CityItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.cityId, item);
    }

    private static CityItem createCityItem(int position) {
        return new CityItem("Item " + position);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class CityItem {
        public String cityId;

        public CityItem(){

        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String city) {
            this.cityId = city;
        }

        public CityItem(String cityId) { this.cityId = cityId; }

        @Override
        public String toString() {
            return cityId;
        }
    }
}
