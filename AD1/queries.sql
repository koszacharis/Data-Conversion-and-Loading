SELECT COUNT(*) AS UsersCount FROM ((SELECT SellerID FROM Sellers) UNION (SELECT BidderID FROM Bidders)) as Users;

SELECT COUNT(*) AS NYItems FROM Items WHERE BINARY Location = 'New York';

SELECT COUNT(*) AS AuctionsCount FROM (SELECT ItemID FROM Categories GROUP BY ItemID HAVING COUNT(*) = 4) as ItemCount;

SELECT ItemID FROM Items WHERE NumberofBids > 0 AND Started < "2001-12-20 00:00:00" AND Ends > "2001-12-20 00:00:01" AND Currently = (SELECT MAX(Currently) FROM Items WHERE NumberofBids > 0 AND Started < "2001-12-20 00:00:00" AND Ends > "2001-12-20 00:00:01");

SELECT COUNT(*) AS TopSellers FROM Sellers WHERE Rating > 1000;

SELECT COUNT(*) AS SellersBidders FROM Sellers S, Bidders B WHERE S.SellerID = B.BidderID;

SELECT COUNT(*) AS CategoriesCount FROM (SELECT COUNT(*) FROM Categories c, Bids b WHERE c.ItemID = b.ItemID AND b.Amount > 100 GROUP BY c.Categories) AS CC;
