package esgi.meteoapp.favourite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class FavouriteContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<FavouriteItem> ITEMS = new ArrayList<FavouriteItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, FavouriteItem> ITEM_MAP = new HashMap<String, FavouriteItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createFavouriteItem(i));
        }
    }

    private static void addItem(FavouriteItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.cityId, item);
    }

    private static FavouriteItem createFavouriteItem(int position) {
        return new FavouriteItem("Item " + position, makeDetails(position));
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
    public static class FavouriteItem {

        public String cityId;
        public String userId;

        public FavouriteItem(){

        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String city) {
            this.cityId = city;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public FavouriteItem(String cityId, String userId) {
            this.cityId = cityId;
            this.userId = userId;
        }

        @Override
        public String toString() {
            return cityId;
        }
    }
}
