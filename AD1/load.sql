LOAD DATA LOCAL INFILE 'bidders.csv' into TABLE Bidders
FIELDS TERMINATED BY '|*|' ;

LOAD DATA LOCAL INFILE 'items.csv' into TABLE Items
FIELDS TERMINATED BY '|*|' ;
  
LOAD DATA LOCAL INFILE 'bids.csv' into TABLE Bids
FIELDS TERMINATED BY '|*|' ;
  
LOAD DATA LOCAL INFILE 'coords.csv' into TABLE Coordinates
FIELDS TERMINATED BY '|*|' ;

LOAD DATA LOCAL INFILE 'categories.csv' into TABLE Categories
FIELDS TERMINATED BY '|*|' ;

LOAD DATA LOCAL INFILE 'categoriesList.csv' into TABLE CategoriesList
FIELDS TERMINATED BY '|*|' ;

LOAD DATA LOCAL INFILE 'sellers.csv' into TABLE Sellers
FIELDS TERMINATED BY '|*|' ;
