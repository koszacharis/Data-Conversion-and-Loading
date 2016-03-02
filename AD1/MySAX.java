
/* Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 */
import java.io.*;
import java.text.*;
import java.util.*;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;

public class MySAX extends DefaultHandler {
    
    public MySAX() {
        
        super();
    }
    
    // used to print to the .csv files
    static PrintWriter tableItems = null;
    static PrintWriter tableBidders = null;
    static PrintWriter tableSellers = null;
    static PrintWriter tableBids = null;
    static PrintWriter tableCategories = null;
    static PrintWriter tableCoords = null;
    static PrintWriter tableCategoriesList = null;
    
    // count xml files
    private static int countDoc = 1;
    // unique categories ID
    int catID = 0;
    // parsed text
    StringBuffer parsedText = new StringBuffer();
    
    String itemName = "";
    String itemID = "";
    String itemCurrently = "";
    String itemBuyPrice = "";
    String itemFirstBid = "";
    String itemNumberOfBids;
    String itemStarted = "";
    String itemEnds = "";
    String itemDesc = "";
    String itemLongitude = "";
    String itemLatitude = "";
    String location = "";
    String country = "";
    String sellerID = "";
    String sellerRating = "";
    String category = "";
    String bidderID = "";
    String bidderCountry = "";
    String bidderLocation = "";
    String bidAmount = "";
    String bidTime = "";
    String bidderRating = "";
    
    Boolean newBidder = false;
    Boolean newSeller = false;
    Boolean newCategory = false;
    
    // store unique BidderID
    static HashSet<String> bidderSet = new HashSet<String>();
    // store unique SellerID
    static HashSet<String> sellerSet = new HashSet<String>();
    // key category name , value category ID
    static HashMap<String, Integer> categoriesMap = new HashMap<String, Integer>();
    
    public static void main(String args[]) throws Exception {
        
        System.out.println("Xml parsing started!");
        long startTime = System.currentTimeMillis();
        
        try {
            // instantiate PrintWriter(s)
            tableItems = new PrintWriter(new FileWriter("items.csv", true));
            tableBidders = new PrintWriter(new FileWriter("bidders.csv", true));
            tableSellers = new PrintWriter(new FileWriter("sellers.csv", true));
            tableCategories = new PrintWriter(new FileWriter("categories.csv", true));
            tableCategoriesList = new PrintWriter(new FileWriter("categoriesList.csv", true));
            tableBids = new PrintWriter(new FileWriter("bids.csv", true));
            tableCoords = new PrintWriter(new FileWriter("coords.csv", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        XMLReader xr = XMLReaderFactory.createXMLReader();
        MySAX handler = new MySAX();
        xr.setContentHandler(handler);
        xr.setErrorHandler(handler);
        
        // Parse each file provided on the command line.
        for (int i = 0; i < args.length; i++) {
            FileReader r = new FileReader(args[i]);
            xr.parse(new InputSource(r));
        }
        
        // close PrintWriter(s)
        tableBids.close();
        tableCategories.close();
        tableCategoriesList.close();
        tableItems.close();
        tableBidders.close();
        tableSellers.close();
        tableCoords.close();
        
        // print
        System.out.println(countDoc - 1 + " xml documents parsed successfully!");
        long endTime = System.currentTimeMillis();
        System.out.println("Xml files parsing completed in " + (endTime - startTime) + " milliseconds");
    }
    
    /**
     * Returns the amount (in XXXXX.xx format) denoted by a money-string like
     * $3,453.23. Returns the input if the input is an empty string.
     *
     * @param money
     * @return formated money
     */
    public String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try {
                am = nf.parse(money).doubleValue();
            } catch (ParseException e) {
                System.out.println("This method should work for all " + "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /**
     * Parses the date in the xml and returns a new formatted date suitable for
     * mySQL.
     *
     * @param time
     * @return formated time
     */
    public String formatDate(String time) {
        SimpleDateFormat inputDate = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String outputDate = "";
        try {
            Date parsed = inputDate.parse(time);
            outputDate = outputFormat.format(parsed);
        } catch (ParseException pe) {
            System.out.println("ERROR: Cannot parse \"" + time + "\"");
        }
        return outputDate;
    }
    
    ////////////////////////////////////////////////////////////////////
    // Event handlers.
    ////////////////////////////////////////////////////////////////////
    
    public void startDocument() {
        
    }
    
    public void endDocument() {
        countDoc++;
        
    }
    
    public void startElement(String uri, String name, String elementName, Attributes atts) {
        
        if (elementName.equals("Bid")) {
            if (elementName.equals("Location")) {
                location = parsedText.toString().trim();
            } else if (elementName.equals("Country")) {
                country = parsedText.toString().trim();
            }
        }
        
        for (int i = 0; i < atts.getLength(); i++) {
            if (elementName.equals("Item")) {
                if (atts.getLocalName(i).equals("ItemID")) {
                    itemID = atts.getValue(i);
                }
            } else if (elementName.equals("Seller")) {
                if (atts.getLocalName(i).equals("Rating")) {
                    sellerRating = atts.getValue(i);
                }
                
                else if (atts.getLocalName(i).equals("UserID")) {
                    sellerID = atts.getValue(i);
                    // check if sellerID already exists
                    if (!sellerSet.contains(sellerID)) {
                        // add new sellerID
                        sellerSet.add(sellerID);
                        newSeller = true;
                    } else {
                        newSeller = false;
                    }
                    // System.out.println("Seller :" + sellerID + " added");
                }
                
            } else if (elementName.equals("Bidder")) {
                if (atts.getLocalName(i).equals("UserID")) {
                    bidderID = atts.getValue(i);
                    // check if bidderID already exists
                    if (!bidderSet.contains(bidderID)) {
                        // add new bidderID
                        bidderSet.add(bidderID);
                        newBidder = true;
                        
                    } else {
                        newBidder = false;
                    }
                } else if (atts.getLocalName(i).equals("Rating")) {
                    bidderRating = atts.getValue(i);
                }
            } else if (elementName.equals("Location")) {
                // get Longitude
                if (atts.getLocalName(i).equals("Longitude")) {
                    itemLongitude = atts.getValue(i);
                }
                // get Latitude
                else if (atts.getLocalName(i).equals("Latitude")) {
                    itemLatitude = atts.getValue(i);
                }
            }
            
        }
        
        // Clear StringBuffer
        parsedText.setLength(0);
    }
    
    public void endElement(String uri, String name, String elementName) {
        
        CategoryList categoryList = new CategoryList();
        
        if ("".equals(uri)) {
            
            if (elementName.equals("Name")) {
                itemName = parsedText.toString().trim();
            }
            
            else if (elementName.equals("Started")) {
                itemStarted = formatDate(parsedText.toString().trim());
            }
            
            else if (elementName.equals("Ends")) {
                itemEnds = formatDate(parsedText.toString().trim());
            }
            
            else if (elementName.equals("Category")) {
                category = parsedText.toString().trim();
                
                // if category does not already exist
                if (!categoriesMap.containsKey(category)) {
                    catID++;
                    newCategory = true;
                    categoriesMap.put(category, catID);
                    
                    categoryList.setCategoryList(category, catID);
                    categoryList.writeToFile();
                } else {
                    newCategory = false;
                }
                tableCategories.append(itemID + "|*|" + categoriesMap.get(category) + "\n");
            }
            
            else if (elementName.equals("Buy_Price")) {
                itemBuyPrice = parsedText.toString().trim();
            }
            
            else if (elementName.equals("Currently")) {
                itemCurrently = strip(parsedText.toString().trim());
            }
            
            else if (elementName.equals("First_Bid")) {
                itemFirstBid = strip(parsedText.toString().trim());
            }
            
            else if (elementName.equals("Number_of_Bids")) {
                itemNumberOfBids = parsedText.toString().trim();
            }
            
            else if (elementName.equals("Description")) {
                itemDesc = parsedText.toString().trim().substring(0,4000);
            }
            
            else if (elementName.equals("Location")) {
                location = parsedText.toString().trim();
                
            } else if (elementName.equals("Country")) {
                country = parsedText.toString().trim();
            }
            
            else if (elementName.equals("Amount")) {
                bidAmount = strip(parsedText.toString().trim());
            } else if (elementName.equals("Time")) {
                bidTime = formatDate(parsedText.toString().trim());
            }
            
            // if Buy_Price does not exist replace with -1.00
            if (itemBuyPrice.equals("")) {
                itemBuyPrice = "-1.00";
            }
            
            // Prepare for output in items csv
            Items items = new Items();
            items.setItems(itemID, itemName, itemCurrently, itemBuyPrice, itemFirstBid, itemNumberOfBids, itemStarted,
                           itemEnds, itemDesc, location, country);
            
            // Prepare for output in coords csv
            Coordinates coordinates = new Coordinates();
            coordinates.setCoordinates(itemID, itemLatitude, itemLongitude);
            
            // when Item element ends, print the data to csv
            if (elementName.equals("Item")) {
                items.writeToFile();
                
                // if latitude and longitude are not empty
                // write to Coords csv
                if (!itemLatitude.equals("") && !itemLongitude.equals("")) {
                    
                    coordinates.writeToFile();
                }
                // reset latitude and longitude
                itemLongitude = "";
                itemLatitude = "";
            }
            
            // write Bids csv
            Bids bids = new Bids();
            if (elementName.equals("Bid")) {
                
                bids.setBids(itemID, bidderID, bidTime, bidAmount);
                bids.writeToFile();
            }
            
            // write Bidder csv
            Bidder bidder = new Bidder();
            if (elementName.equals("Bidder")) {
                // if location or country empty, put -1
                if (location.equals("")) {
                    location = " ";
                }
                if (country.equals("")) {
                    country = " ";
                }
                if (newBidder == true) {
                    
                    bidder.setBidder(bidderID, bidderRating, location, country);
                    bidder.writeToFile();
                }
                
                // reset location and country
                location = "";
                country = "";
                
            }
            
            // write Sellers csv
            Seller seller = new Seller();
            if (elementName.equals("Seller")) {
                // check if SellerID is new
                if (newSeller == true) {
                    seller.setSeller(sellerID, sellerRating);
                    seller.writeToFile();
                }
            }
        } else
            System.out.println("End element:   {" + uri + "}" + name);
    }
    
    public void characters(char[] ch, int start, int length) {
        
        parsedText.append(ch, start, length);
        
    }
    
    public class Bidder {
        
        private String bidderID;
        private String bidderRating;
        private String bidderLocation;
        private String bidderCountry;
        
        public void setBidder(String bidderID, String bidderRating, String bidderLocation, String bidderCountry) {
            this.bidderID = bidderID;
            this.bidderRating = bidderRating;
            this.bidderLocation = bidderLocation;
            this.bidderCountry = bidderCountry;
        }
        
        public void writeToFile() {
            tableBidders
            .append(bidderID + "|*|" + bidderRating + "|*|" + bidderLocation + "|*|" + bidderCountry + "\n");
        }
        
    }
    
    public class Items {
        
        private String itemID;
        private String itemName;
        private String itemCurrently;
        private String itemBuyPrice;
        private String itemStarted;
        private String itemNumberOfBids;
        private String itemFirstBid;
        private String itemEnds;
        private String itemDesc;
        private String location;
        private String country;
        
        public void setItems(String itemID, String itemName, String itemCurrently, String itemBuyPrice,
                             String itemFirstBid, String itemNumberOfBids, String itemStarted, String itemEnds, String itemDesc,
                             String location, String country) {
            this.itemID = itemID;
            this.itemName = itemName;
            this.itemBuyPrice = itemBuyPrice;
            this.itemCurrently = itemCurrently;
            this.itemFirstBid = itemFirstBid;
            this.itemNumberOfBids = itemNumberOfBids;
            this.itemStarted = itemStarted;
            this.itemEnds = itemEnds;
            this.itemDesc = itemDesc;
            this.location = location;
            this.country = country;
        }
        
        public void writeToFile() {
            tableItems.append(itemID + "|*|" + itemName + "|*|" + itemCurrently + "|*|" + itemBuyPrice + "|*|"
                              + itemFirstBid + "|*|" + itemNumberOfBids + "|*|" + itemStarted + "|*|" + itemEnds + "|*|"
                              + itemDesc + "|*|" + country + "|*|" + location + "\n");
        }
    }
    
    public class Seller {
        
        private String sellerID;
        private String sellerRating;
        
        public void setSeller(String sellerID, String sellerRating) {
            this.sellerID = sellerID;
            this.sellerRating = sellerRating;
        }
        
        public void writeToFile() {
            tableSellers.append(sellerID + "|*|" + sellerRating + "\n");
            
        }
    }
    
    public class Bids {
        
        private String itemID;
        private String bidderID;
        private String bidTime;
        private String bidAmount;
        
        public void setBids(String itemID, String bidderID, String bidTime, String bidAmount) {
            this.itemID = itemID;
            this.bidderID = bidderID;
            this.bidTime = bidTime;
            this.bidAmount = bidAmount;
            
        }
        
        public void writeToFile() {
            tableBids.append(itemID + "|*|" + bidderID + "|*|" + bidTime + "|*|" + bidAmount + "\n");
        }
    }
    
    public class Coordinates {
        
        private String itemID;
        private String longitude;
        private String latitude;
        
        public void setCoordinates(String itemID, String latitude, String longitude) {
            this.itemID = itemID;
            this.longitude = longitude;
            this.latitude = latitude;
        }
        
        public void writeToFile() {
            tableCoords.append(itemID + "|*|" + longitude + "|*|" + latitude + "\n");
        }
        
    }
    
    public class CategoryList {
        
        private int catID;
        private String category;
        
        public void setCategoryList(String category, int catID) {
            this.catID = catID;
            this.category = category;
        }
        
        public void writeToFile() {
            tableCategoriesList.append(category + "|*|" + catID + "\n");
        }
    }
    
}