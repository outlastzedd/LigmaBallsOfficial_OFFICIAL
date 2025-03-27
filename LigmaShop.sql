CREATE TABLE cart (
    cartid SERIAL PRIMARY KEY,
    createddate DATE NOT NULL,
    userid INTEGER NOT NULL
);

CREATE TABLE cartitems (
    cartitemid SERIAL PRIMARY KEY,
    addeddate TIMESTAMP(6) NOT NULL,
    quantity INTEGER NOT NULL,
    cartid INTEGER NOT NULL,
    productsizecolorid INTEGER NOT NULL
);

CREATE TABLE categories (
    categoryid SERIAL PRIMARY KEY,
    categoryname VARCHAR(255) NOT NULL,
    description VARCHAR(500)
);

CREATE TABLE chatmessages (
    messageid SERIAL PRIMARY KEY,
    messagetext VARCHAR(255) NOT NULL,
    sender VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP(6) NOT NULL,
    sessionid INTEGER NOT NULL
);

CREATE TABLE chatsessions (
    sessionid SERIAL PRIMARY KEY,
    endtime TIMESTAMP(6),
    starttime TIMESTAMP(6) NOT NULL,
    userid INTEGER
);

CREATE TABLE colors (
    colorid SERIAL PRIMARY KEY,
    colorname VARCHAR(50) NOT NULL,
    description VARCHAR(50)
);

CREATE TABLE company (
    companyid SERIAL PRIMARY KEY,
    companyname VARCHAR(255) NOT NULL,
    address VARCHAR(100),
    contactnumber VARCHAR(15),
    email VARCHAR(255)
);

CREATE TABLE inventory (
    inventoryid SERIAL PRIMARY KEY,
    productsizecolorid INTEGER,
    stock INTEGER,
    lastupdated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orderdetails (
    orderdetailid SERIAL PRIMARY KEY,
    orderid INTEGER NOT NULL,
    productsizecolorid INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    price NUMERIC(38,2)
);

CREATE TABLE orders (
    orderid SERIAL PRIMARY KEY,
    userid INTEGER NOT NULL,
    orderdate DATE NOT NULL DEFAULT CURRENT_DATE,
    totalamount NUMERIC(38,2),
    paymentmethodid INTEGER
);

CREATE TABLE orderstatus (
    orderid INTEGER PRIMARY KEY,
    statusname VARCHAR(50) NOT NULL
);

CREATE TABLE paymentmethods (
    paymentmethodid SERIAL PRIMARY KEY,
    methodname VARCHAR(255) NOT NULL,
    description TEXT,
    isactive BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE productcategories (
    productcategoryid SERIAL PRIMARY KEY,
    productid INTEGER NOT NULL,
    categoryid INTEGER NOT NULL
);

CREATE TABLE productimages (
    imageid SERIAL PRIMARY KEY,
    productid INTEGER NOT NULL,
    imageurl TEXT
);

CREATE TABLE products (
    productid SERIAL PRIMARY KEY,
    productname VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    price NUMERIC(38,2),
    createddate DATE NOT NULL,
    discount INTEGER,
    status BOOLEAN DEFAULT TRUE,
    companyid INTEGER,
    rating NUMERIC(38,2) DEFAULT 0
);

CREATE TABLE productsizecolor (
    productsizecolorid SERIAL PRIMARY KEY,
    productid INTEGER NOT NULL,
    sizeid INTEGER NOT NULL,
    colorid INTEGER NOT NULL,
   	price NUMERIC(38,2)
);

CREATE TABLE productviews (
    viewid SERIAL PRIMARY KEY,
    productid INTEGER,
    userid INTEGER,
    viewdate DATE
);

CREATE TABLE reviews (
    reviewid SERIAL PRIMARY KEY,
    userid INTEGER NOT NULL,
    productid INTEGER NOT NULL,
    rating INTEGER NOT NULL,
    comment VARCHAR(500),
    reviewdate DATE NOT NULL
);

CREATE TABLE shipping (
    shippingid SERIAL PRIMARY KEY,
    orderid INTEGER,
    shippingcompanyid INTEGER,
    address VARCHAR(200) NOT NULL,
    shippingmethod VARCHAR(255) NOT NULL,
    shippingstatus VARCHAR(50) NOT NULL,
    estimateddeliverydate DATE,
    actualdeliverydate DATE,
    trackingnumber VARCHAR(255)
);

CREATE TABLE shippingcompanies (
    shippingcompanyid SERIAL PRIMARY KEY,
    companyname VARCHAR(255) NOT NULL,
    address VARCHAR(100)
);

CREATE TABLE sizes (
    sizeid SERIAL PRIMARY KEY,
    sizename VARCHAR(50) NOT NULL,
    description VARCHAR(100)
);

CREATE TABLE users (
    userid SERIAL PRIMARY KEY,
    fullname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phonenumber VARCHAR(15),
    address VARCHAR(100),
    role VARCHAR(10) DEFAULT 'user' CHECK (role IN ('admin', 'user')),
    status BOOLEAN DEFAULT TRUE
);

CREATE TABLE voucher (
    voucherid SERIAL PRIMARY KEY,
    voucherday DATE,
    discountvalue INTEGER
);

-- Add foreign key constraints
ALTER TABLE cart 
    ADD CONSTRAINT "FK_CART_USERS" 
    FOREIGN KEY (userid) 
    REFERENCES users (userid);

ALTER TABLE cartitems 
    ADD CONSTRAINT "FK_CARTITEMS_CART" 
    FOREIGN KEY (cartid) 
    REFERENCES cart (cartid),
    ADD CONSTRAINT "FK_CARTITEMS_PRODUCTSIZECOLOR" 
    FOREIGN KEY (productsizecolorid) 
    REFERENCES productsizecolor (productsizecolorid);

ALTER TABLE inventory 
    ADD CONSTRAINT "FK_INVENTORY_PRODUCTSIZECOLOR" 
    FOREIGN KEY (productsizecolorid) 
    REFERENCES productsizecolor (productsizecolorid) 
    ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE orderdetails 
    ADD CONSTRAINT "FK_ORDERDETAILS_ORDERS" 
    FOREIGN KEY (orderid) 
    REFERENCES orders (orderid) 
    ON UPDATE CASCADE ON DELETE CASCADE,
    ADD CONSTRAINT "FK_ORDERDETAILS_PRODUCTSIZECOLOR" 
    FOREIGN KEY (productsizecolorid) 
    REFERENCES productsizecolor (productsizecolorid) 
    ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE orders
    ADD CONSTRAINT "FK_ORDERS_PAYMENTMETHODS" 
    FOREIGN KEY (paymentmethodid) 
    REFERENCES paymentmethods (paymentmethodid) 
    ON UPDATE CASCADE ON DELETE CASCADE,
    ADD CONSTRAINT "FK_ORDERS_USERS" 
    FOREIGN KEY (userid) 
    REFERENCES users (userid) 
    ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE orderstatus 
    ADD CONSTRAINT "FK_ORDERSTATUS_ORDERS" 
    FOREIGN KEY (orderid) 
    REFERENCES orders (orderid) 
    ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE productcategories 
    ADD CONSTRAINT "FK_ProductCategories_CATEGORIES" 
    FOREIGN KEY (categoryid) 
    REFERENCES categories (categoryid) 
    ON UPDATE CASCADE ON DELETE CASCADE,
    ADD CONSTRAINT "FK_ProductCategories_PRODUCTS" 
    FOREIGN KEY (productid) 
    REFERENCES products (productid) 
    ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE productimages 
    ADD CONSTRAINT "FK_PRODUCTIMAGES_PRODUCTS" 
    FOREIGN KEY (productid) 
    REFERENCES products (productid) 
    ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE products 
    ADD CONSTRAINT "FK_PRODUCTS_COMPANY" 
    FOREIGN KEY (companyid) 
    REFERENCES company (companyid) 
    ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE productsizecolor 
    ADD CONSTRAINT "FK_PRODUCTSIZECOLOR_COLORS" 
    FOREIGN KEY (colorid) 
    REFERENCES colors (colorid) 
    ON UPDATE CASCADE ON DELETE CASCADE,
    ADD CONSTRAINT "FK_PRODUCTSIZECOLOR_PRODUCTS" 
    FOREIGN KEY (productid) 
    REFERENCES products (productid) 
    ON UPDATE CASCADE ON DELETE CASCADE,
    ADD CONSTRAINT "FK_PRODUCTSIZECOLOR_SIZES" 
    FOREIGN KEY (sizeid) 
    REFERENCES sizes (sizeid) 
    ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE productviews 
    ADD CONSTRAINT "FK_PRODUCTVIEWS_PRODUCTS" 
    FOREIGN KEY (productid) 
    REFERENCES products (productid) 
    ON UPDATE CASCADE ON DELETE CASCADE,
    ADD CONSTRAINT "FK_PRODUCTVIEWS_USERS" 
    FOREIGN KEY (userid) 
    REFERENCES users (userid) 
    ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE reviews 
    ADD CONSTRAINT "FK_REVIEWS_PRODUCTS" 
    FOREIGN KEY (productid) 
    REFERENCES products (productid) 
    ON UPDATE CASCADE ON DELETE CASCADE,
    ADD CONSTRAINT "FK_REVIEWS_USERS" 
    FOREIGN KEY (userid) 
    REFERENCES users (userid) 
    ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE shipping 
    ADD CONSTRAINT "FK_Shipping_ORDERS" 
    FOREIGN KEY (orderid) 
    REFERENCES orders (orderid) 
    ON UPDATE CASCADE ON DELETE CASCADE,
    ADD CONSTRAINT "FK_Shipping_ShippingCompanies" 
    FOREIGN KEY (shippingcompanyid) 
    REFERENCES shippingcompanies (shippingcompanyid) 
    ON UPDATE CASCADE ON DELETE CASCADE;

-- ***** Insert data *****

-- Insert into USERS
INSERT INTO users (fullname, email, password, phonenumber, address, role, status)
VALUES 
    ('Nguyen Minh Hieu', 'admgnusam123@gmail.com', 'daden69420', '0357116420', 'Nigeria', 'admin', TRUE),
    ('Dinh Huy Hoang', 'hahoang05092004@gmail.com', 'daden2', '0829701569', 'HCM City', 'admin', TRUE),
    ('Nguyen Duc Huy Hoang', 'hoangndhde180637@fpt.edu.vn', 'daden3', '0899886249', 'Da Nang', 'user', TRUE),
    ('Le Xuan Hoang', 'lehoang160204@gmail.com', 'daden4', '0706160204', 'Hue', 'user', TRUE),
    ('Le Thanh Dat', 'lethanhdat23062004@gmail.com', 'daden5', '0848884158', 'Can Tho', 'user', TRUE),
    ('Nguyen Dinh Duy', 'ndduy269@gmail.com', 'daden6', '0392858466', 'Hai Phong', 'user', TRUE),
    ('Hoang Cong Binh', 'huyhoang05092004hoa@gmail.com', 'daden7', '0945566778', 'Quang Ninh', 'user', TRUE);

-- Insert into COMPANY
INSERT INTO company (companyName, address, contactNumber, email)
VALUES 
    ('Company A', '123 Street, Hanoi', '0123456789', 'contact@companya.com'),
    ('Company B', '456 Street, HCM City', '0987654321', 'contact@companyb.com'),
    ('Company C', '789 Street, Da Nang', '0911223344', 'contact@companyc.com'),
    ('Company D', '321 Street, Can Tho', '0933445566', 'contact@companyd.com');

-- Insert into categories
INSERT INTO categories (categoryname, description)
VALUES 
    ('Tất cả sản phẩm', 'Tất cả sản phẩm'),
    ('Quần áo Nam', 'Tất cả quần và áo dành cho Nam'),
    ('Quần áo Nữ', 'Tất cả quần và áo dành cho Nữ'),
    ('Áo Nam', 'Tất cả áo dành cho Nam'),
    ('Áo Nữ', 'Tất cả áo dành cho Nữ'),
    ('Quần Nam', 'Tất cả quần cho Nam'),
    ('Quần Nữ', 'Tất cả quần cho Nữ'),
    ('Áo khoác', 'Tất cả áo khoác'),
    ('Đầm', 'Đầm dành cho nữ'),
    ('Đồ thể thao', 'Đồ thể thao dành cho cả Nam và Nữ'),
    ('Thời trang mùa đông', 'Thời trang dành cho mùa đông'),
    ('Thời trang mùa hè', 'Thời trang dành cho mùa hè');

-- Insert into products
INSERT INTO products (productname, description, price, createddate, discount, companyid)
VALUES
    ('Áo sơ mi Nam', 'Áo sơ mi nam cao cấp', 150000.00, '2025-01-01', 10, 1),
    ('Áo sơ mi Nữ', 'Áo sơ mi nữ thanh lịch', 140000.00, '2025-01-01', 10, 1),
    ('Áo khoác Nam', 'Áo khoác nam thời trang', 250000.00, '2025-01-01', 10, 1),
    ('Áo khoác Nữ', 'Áo khoác nữ đẹp', 240000.00, '2025-01-01', 10, 1),
    ('Áo len Nam', 'Áo len ấm áp cho nam', 200000.00, '2025-01-01', 10, 1),
    ('Áo len Nữ', 'Áo len dày cho nữ', 190000.00, '2025-01-01', 10, 1),
    ('Quần jeans Nam', 'Quần jeans nam thời trang', 300000.00, '2025-01-01', 10, 1),
    ('Quần jeans Nữ', 'Quần jeans nữ phong cách', 290000.00, '2025-01-01', 10, 1),
    ('Quần kaki Nam', 'Quần kaki cho nam lịch lãm', 220000.00, '2025-01-01', 10, 1),
    ('Quần kaki Nữ', 'Quần kaki nữ xinh xắn', 210000.00, '2025-01-01', 10, 1),
    ('Quần short Nam', 'Quần short cho nam trẻ trung', 180000.00, '2025-01-01', 10, 1),
    ('Quần short Nữ', 'Quần short cho nữ năng động', 170000.00, '2025-01-01', 10, 2),
    ('Áo hoodie Nam', 'Áo hoodie cho nam thoải mái', 250000.00, '2025-01-01', 10, 2),
    ('Áo hoodie Nữ', 'Áo hoodie nữ dễ thương', 220000.00, '2025-01-01', 10, 2),
    ('Áo khoác lông Nam', 'Áo khoác lông cho nam sang trọng', 35000.00, '2025-01-01', 10, 2),
    ('Áo khoác lông Nữ', 'Áo khoác lông cho nữ ấm áp', 340000.00, '2025-01-01', 10, 2),
    ('Áo thun dài tay Nam', 'Áo thun dài tay nam mùa đông', 160000.00, '2025-01-01', 10, 2),
    ('Áo thun dài tay Nữ', 'Áo thun dài tay nữ mùa đông', 150000.00, '2025-01-01', 10, 2),
    ('Áo sơ mi họa tiết Nam', 'Áo sơ mi họa tiết nam thời trang', 170000.00, '2025-01-01', 10, 2),
    ('Áo sơ mi họa tiết Nữ', 'Áo sơ mi họa tiết nữ thanh lịch', 160000.00, '2025-01-01', 10, 2),
    ('Áo khoác dáng dài Nam', 'Áo khoác dáng dài cho nam', 400000.00, '2025-01-01', 10, 2),
    ('Áo khoác dáng dài Nữ', 'Áo khoác dáng dài cho nữ', 450000.00, '2025-01-01', 10, 2),
    ('Quần sooc thể thao Nam', 'Quần sooc thể thao nam', 120000.00, '2025-01-01', 10, 2),
    ('Quần sooc thể thao Nữ', 'Quần sooc thể thao nữ', 110000.00, '2025-01-01', 10, 2),
    ('Áo len cổ cao Nam', 'Áo len cổ cao nam', 200000.00, '2025-01-01', 10, 3),
    ('Áo len cổ cao Nữ', 'Áo len cổ cao nữ', 190000.00, '2025-01-01', 10, 3),
    ('Áo vest Nam', 'Áo vest nam thanh lịch', 1500000.00, '2025-01-01', 10, 3),
    ('Áo vest Nữ', 'Áo vest nữ duyên dáng', 1200000.00, '2025-01-01', 15, 3),
    ('Áo croptop Nữ', 'Áo croptop cho nữ', 800000.00, '2025-01-01', 15, 3),
    ('Áo thun ngắn tay Nam', 'Áo thun ngắn tay cho nam', 85000.00, '2025-01-01', 15, 3),
    ('Áo thun ngắn tay Nữ', 'Áo thun ngắn tay cho nữ', 75000.00, '2025-01-01', 15, 3),
    ('Áo dài Nam', 'Áo dài nam truyền thống', 300000.00, '2025-01-01', 15, 3),
    ('Áo dài Nữ', 'Áo dài nữ truyền thống', 280000.00, '2025-01-01', 15, 3),
    ('Đầm dạ hội Nữ', 'Đầm dạ hội nữ sang trọng', 500000.00, '2025-01-01', 15, 3),
    ('Đầm công sở Nữ', 'Đầm công sở nữ thanh lịch', 450000.00, '2025-01-01', 15, 3),
    ('Áo sát nách Nam', 'Áo sát nách nam', 70000.00, '2025-01-01', 15, 4),
    ('Áo sát nách Nữ', 'Áo sát nách nữ', 60000.00, '2025-01-01', 15, 4),
    ('Áo thun thể thao Nam', 'Áo thun thể thao nam', 130000.00, '2025-01-01', 15, 4),
    ('Áo thun thể thao Nữ', 'Áo thun thể thao nữ', 120000.00, '2025-01-01', 15, 4),
    ('Quần legging Nữ', 'Quần legging nữ', 100000.00, '2025-01-01', 15, 4),
    ('Quần legging Nam', 'Quần legging nam', 190000.00, '2025-01-01', 15, 4),
    ('Áo sơ mi kẻ Nam', 'Áo sơ mi kẻ nam', 180000.00, '2025-01-01', 15, 4),
    ('Áo sơ mi kẻ Nữ', 'Áo sơ mi kẻ nữ', 170000.00, '2025-01-01', 20, 4),
    ('Áo chống nắng Nam', 'Áo chống nắng nam', 140000.00, '2025-01-01', 20, 4),
    ('Áo chống nắng Nữ', 'Áo chống nắng nữ', 330000.00, '2025-01-01', 20, 4);

-- Insert into productcategories
INSERT INTO productcategories (productid, categoryid)
SELECT p.productid, c.categoryid
FROM categories c
JOIN products p
ON (   
       (c.categoryname = 'Tất cả sản phẩm' AND (p.productname LIKE 'Áo%' OR p.productname LIKE 'Quần%' OR p.productname LIKE 'Đầm%'))
    OR (c.categoryname = 'Áo Nam' AND p.productname LIKE 'Áo%Nam') 
    OR (c.categoryname = 'Áo Nữ' AND p.productname LIKE 'Áo%Nữ')
    OR (c.categoryname = 'Quần Nam' AND p.productname LIKE 'Quần%Nam')
    OR (c.categoryname = 'Quần Nữ' AND p.productname LIKE 'Quần%Nữ')
    OR (c.categoryname = 'Quần áo Nam' AND (p.productname LIKE 'Áo%Nam' OR p.productname LIKE 'Quần%Nam'))
    OR (c.categoryname = 'Quần áo Nữ' AND (p.productname LIKE 'Áo%Nữ' OR p.productname LIKE 'Quần%Nữ'))
    OR (c.categoryname = 'Áo khoác' AND p.productname LIKE 'Áo%khoác%')
    OR (c.categoryname = 'Đầm' AND p.productname LIKE 'Đầm%')
    OR (c.categoryname = 'Đồ thể thao' AND (p.productname LIKE '%thể thao%' OR p.productname LIKE '%legging%'))
    OR (c.categoryname = 'Thời trang mùa đông' AND (p.productname LIKE '%len%' OR p.productname LIKE '%hoodie%' OR p.productname LIKE '%khoác lông%' OR p.productname LIKE '%jeans%' OR p.productname LIKE '%dài tay%' OR p.productname LIKE '%dáng dài%'))
    OR (c.categoryname = 'Thời trang mùa hè' AND (p.productname LIKE '%sơ mi%' OR p.productname LIKE '%short%' OR p.productname LIKE '%croptop%' OR p.productname LIKE '%ngắn tay%' OR p.productname LIKE '%chống nắng%'))
);

-- Insert into productimages
INSERT INTO productimages (productid, imageurl)
VALUES 
    (1, 'https://pageofme.github.io/team1_prj301/images/soMiNam.jpg'),
    (1, 'https://pageofme.github.io/team1_prj301/images/aoSoMiNamXanh.jpg'),
    (1, 'https://pageofme.github.io/team1_prj301/images/aoSoMiNamDen.jpg'),
    (2, 'https://pageofme.github.io/team1_prj301/images/aoSoMiNu.jpg'),
    (2, 'https://pageofme.github.io/team1_prj301/images/aoSoMiNuXanh.png'),
    (2, 'https://pageofme.github.io/team1_prj301/images/aoSoMiNuDen.jpg'),
    (3, 'https://pageofme.github.io/team1_prj301/images/aoKhoacNam.jpg'),
    (3, 'https://pageofme.github.io/team1_prj301/images/aoKhoacNamDen.jpg'),
    (3, 'https://pageofme.github.io/team1_prj301/images/aoKhoacNamTrang.jpg'),
    (4, 'https://pageofme.github.io/team1_prj301/images/aoKhoacNu.jpg'),
    (4, 'https://pageofme.github.io/team1_prj301/images/aoKhoacNuDen.jpg'),
    (4, 'https://pageofme.github.io/team1_prj301/images/aoKhoacNuTrang.jpg'),
    (5, 'https://pageofme.github.io/team1_prj301/images/aoLenNamTrang.jpg'),
    (5, 'https://pageofme.github.io/team1_prj301/images/aoLenNamXanh.png'),
    (5, 'https://pageofme.github.io/team1_prj301/images/aoLenNamDen.jpg'),
    (6, 'https://pageofme.github.io/team1_prj301/images/aoLenNuDen.jpg'),
    (6, 'https://pageofme.github.io/team1_prj301/images/aoLenNuXanh.jpg'),
    (6, 'https://pageofme.github.io/team1_prj301/images/aoLenNuTrang.jpg'),
    (7, 'https://pageofme.github.io/team1_prj301/images/quanJeanNam.jpg'),
    (7, 'https://pageofme.github.io/team1_prj301/images/quanJeanNamDen.jpg'),
    (7, 'https://pageofme.github.io/team1_prj301/images/quanJeanNamTrang.jpg'),
    (8, 'https://pageofme.github.io/team1_prj301/images/quanJeanNuXanh.jpg'),
    (8, 'https://pageofme.github.io/team1_prj301/images/quanJeanNuTrang.jpg'),
    (8, 'https://pageofme.github.io/team1_prj301/images/quanJeanNuDen.jpg'),
    (9, 'https://pageofme.github.io/team1_prj301/images/quanKakiNam.jpg'),
    (9, 'https://pageofme.github.io/team1_prj301/images/quanKakiNamXanh.jpg'),
    (9, 'https://pageofme.github.io/team1_prj301/images/quanKakiNamDen.jpg'),
    (10, 'https://pageofme.github.io/team1_prj301/images/quanKakiNuXanh.jpg'),
    (10, 'https://pageofme.github.io/team1_prj301/images/quanKakiNuDen.jpg'),
    (10, 'https://pageofme.github.io/team1_prj301/images/quanKakiNuTrang.png'),
    (11, 'https://pageofme.github.io/team1_prj301/images/quanShortNam.jpg'),
    (11, 'https://pageofme.github.io/team1_prj301/images/quanShortNamDen.jpg'),
    (11, 'https://pageofme.github.io/team1_prj301/images/quanShortNamXanh.jpg'),
    (12, 'https://pageofme.github.io/team1_prj301/images/quanShortNu.jpg'),
    (12, 'https://pageofme.github.io/team1_prj301/images/quanShortNuXanh.jpg'),
    (12, 'https://pageofme.github.io/team1_prj301/images/quanShortNuTrang.jpg'),
    (13, 'https://pageofme.github.io/team1_prj301/images/aoHoodieNamDen.jpg'),
    (13, 'https://pageofme.github.io/team1_prj301/images/aoHoodieNamXanh.jpg'),
    (13, 'https://pageofme.github.io/team1_prj301/images/aoHoodieNamTrang.jpg'),
    (14, 'https://pageofme.github.io/team1_prj301/images/aoHoodieNu.jpg'),
    (14, 'https://pageofme.github.io/team1_prj301/images/aoHoodieNuDen.jpg'),
    (14, 'https://pageofme.github.io/team1_prj301/images/aoHoodieNuXanh.jpg'),
    (15, 'https://pageofme.github.io/team1_prj301/images/aoKhoacLongNamDen.jpg'),
    (15, 'https://pageofme.github.io/team1_prj301/images/aoKhoacLongNamXanh.jpg'),
    (15, 'https://pageofme.github.io/team1_prj301/images/aoKhoacLongNamTrang.jpg'),
    (16, 'https://pageofme.github.io/team1_prj301/images/aoKhoacLongNuXanh.jpg'),
    (16, 'https://pageofme.github.io/team1_prj301/images/aoKhoacLongNuTrang.jpg'),
    (16, 'https://pageofme.github.io/team1_prj301/images/aoKhoacLongNuDen.jpg'),
    (17, 'https://pageofme.github.io/team1_prj301/images/aoThunDaiTayNamXanh.jpg'),
    (17, 'https://pageofme.github.io/team1_prj301/images/aoThunDaiTayNamTrang.jpg'),
    (17, 'https://pageofme.github.io/team1_prj301/images/aoThunDaiTayNamDen.jpg'),
    (18, 'https://pageofme.github.io/team1_prj301/images/aoThunDaiTayNuDen.jpg'),
    (18, 'https://pageofme.github.io/team1_prj301/images/aoThunDaiTayNuTrang.jpg'),
    (18, 'https://pageofme.github.io/team1_prj301/images/aoThunDaiTayNuXanh.jpg'),
    (19, 'https://pageofme.github.io/team1_prj301/images/aoSoMiHoaTietNam.jpg'),
    (19, 'https://pageofme.github.io/team1_prj301/images/aoSoMiHoaTietNamDen.jpg'),
    (19, 'https://pageofme.github.io/team1_prj301/images/aoSoMiHoaTietNamXanh.jpg'),
    (20, 'https://pageofme.github.io/team1_prj301/images/aoSoMiHoaTietNuTrang.jpg'),
    (20, 'https://pageofme.github.io/team1_prj301/images/aoSoMiHoaTietNuXanh.jpg'),
    (20, 'https://pageofme.github.io/team1_prj301/images/aoSoMiHoaTietNuDen.jpg'),
    (21, 'https://pageofme.github.io/team1_prj301/images/aoKhoacDangDaiNamXanh.webp'),
    (21, 'https://pageofme.github.io/team1_prj301/images/aoKhoacDangDaiNamTrang.jpg'),
    (21, 'https://pageofme.github.io/team1_prj301/images/aoKhoacDangDaiNamDen.jpg'),
    (22, 'https://pageofme.github.io/team1_prj301/images/aoKhoacDangDaiNu.jpg'),
    (22, 'https://pageofme.github.io/team1_prj301/images/aoKhoacDangDaiNuTrang.webp'),
    (22, 'https://pageofme.github.io/team1_prj301/images/aoKhoacDangDaiNuXanh.webp'),
    (23, 'https://pageofme.github.io/team1_prj301/images/quanSoocTheThaoNam.jpeg'),
    (23, 'https://pageofme.github.io/team1_prj301/images/quanShortTheThaoNamXanh.jpg'),
    (23, 'https://pageofme.github.io/team1_prj301/images/quanShortTheThaoNamTrang.jpg'),
    (24, 'https://pageofme.github.io/team1_prj301/images/quanSoocTheThaoNu.jpg'),
    (24, 'https://pageofme.github.io/team1_prj301/images/quanShortTheThaoNuTrang.webp'),
    (24, 'https://pageofme.github.io/team1_prj301/images/quanShortTheThaoNuXanh.png'),
    (25, 'https://pageofme.github.io/team1_prj301/images/aoLenCoCaoNam.jpg'),
    (25, 'https://pageofme.github.io/team1_prj301/images/aoLenCoCaoNamXanh.webp'),
    (25, 'https://pageofme.github.io/team1_prj301/images/aoLenCoCaoNamDen.jpg'),
    (26, 'https://pageofme.github.io/team1_prj301/images/aoLenCoCaoNuTrang.jpg'),
    (26, 'https://pageofme.github.io/team1_prj301/images/aoLenCoCaoNuDen.jpg'),
    (26, 'https://pageofme.github.io/team1_prj301/images/aoLenCoCaoNuXanh.jpg'),
    (27, 'https://pageofme.github.io/team1_prj301/images/aoVestNam.jpg'),
    (27, 'https://pageofme.github.io/team1_prj301/images/aoVestNamXanh.jpg'),
    (27, 'https://pageofme.github.io/team1_prj301/images/aoVestNamTrang.webp'),
    (28, 'https://pageofme.github.io/team1_prj301/images/aoVestNu.jpg'),
    (28, 'https://pageofme.github.io/team1_prj301/images/aoVestNuDen.jpg'),
    (28, 'https://pageofme.github.io/team1_prj301/images/aoVestNuTrang.jpg'),
    (29, 'https://pageofme.github.io/team1_prj301/images/aoCroptopXanh.jpg'),
    (29, 'https://pageofme.github.io/team1_prj301/images/aoCroptopDen.webp'),
    (29, 'https://pageofme.github.io/team1_prj301/images/aoCroptopTrang.jpg'),
    (30, 'https://pageofme.github.io/team1_prj301/images/aoThunNganTayNamXanh.jpg'),
    (30, 'https://pageofme.github.io/team1_prj301/images/aoThunNganTayNamTrang.webp'),
    (30, 'https://pageofme.github.io/team1_prj301/images/aoThunNganTayNamDen.jpeg'),
    (31, 'https://pageofme.github.io/team1_prj301/images/aoThunNganTayNuDen.jpg'),
    (31, 'https://pageofme.github.io/team1_prj301/images/aoThunNganTayNuTrang.jpg'),
    (31, 'https://pageofme.github.io/team1_prj301/images/aoThunNganTayNuXanh.webp'),
    (32, 'https://pageofme.github.io/team1_prj301/images/aoDaiNamXanh.jpg'),
    (32, 'https://pageofme.github.io/team1_prj301/images/aoDaiNamDen.jpg'),
    (32, 'https://pageofme.github.io/team1_prj301/images/aoDaiNamTrang.webp'),
    (33, 'https://pageofme.github.io/team1_prj301/images/aoDaiNuDen.jpg'),
    (33, 'https://pageofme.github.io/team1_prj301/images/aoDaiNuTrang.jpg'),
    (33, 'https://pageofme.github.io/team1_prj301/images/aoDaiNuXanh.webp'),
    (34, 'https://pageofme.github.io/team1_prj301/images/damDaHoiXanh.jpg'),
    (34, 'https://pageofme.github.io/team1_prj301/images/damDaHoiTrang.jpg'),
    (34, 'https://pageofme.github.io/team1_prj301/images/damDaHoiDen.png'),
    (35, 'https://pageofme.github.io/team1_prj301/images/damCongSoDen.jpg'),
    (35, 'https://pageofme.github.io/team1_prj301/images/damCongSoTrang.jpeg'),
    (35, 'https://pageofme.github.io/team1_prj301/images/damCongSoXanh.jpg'),
    (36, 'https://pageofme.github.io/team1_prj301/images/aoSatNachNam.jpg'),
    (36, 'https://pageofme.github.io/team1_prj301/images/aoSatNachNamDen.png'),
    (36, 'https://pageofme.github.io/team1_prj301/images/aoSatNachNamXanh.jpg'),
    (37, 'https://pageofme.github.io/team1_prj301/images/aoSatNachNu.jpg'),
    (37, 'https://pageofme.github.io/team1_prj301/images/aoSatNachNuDen.jpg'),
    (37, 'https://pageofme.github.io/team1_prj301/images/aoSatNachNuTrang.jpg'),
    (38, 'https://pageofme.github.io/team1_prj301/images/aoThunTheThaoNamXanh.jpg'),
    (38, 'https://pageofme.github.io/team1_prj301/images/aoThunTheThaoNamTrang.jpg'),
    (38, 'https://pageofme.github.io/team1_prj301/images/aoThunTheThaoNamDen.jpg'),
    (39, 'https://pageofme.github.io/team1_prj301/images/aoThunTheThaoNuDen.jpg'),
    (39, 'https://pageofme.github.io/team1_prj301/images/aoThunTheThaoNuXanh.png'),
    (39, 'https://pageofme.github.io/team1_prj301/images/aoThunTheThaoNuTrang.webp'),
    (40, 'https://pageofme.github.io/team1_prj301/images/quanLeggingNu.jpg'),
    (40, 'https://pageofme.github.io/team1_prj301/images/quanLeggingNuTrang.jpg'),
    (40, 'https://pageofme.github.io/team1_prj301/images/quanLeggingNuXanh.png'),
    (41, 'https://pageofme.github.io/team1_prj301/images/quanLeggingNam.jpg'),
    (41, 'https://pageofme.github.io/team1_prj301/images/quanLeggingNamXanh.jpg'),
    (41, 'https://pageofme.github.io/team1_prj301/images/quanLeggingNamTrang.jpg'),
    (42, 'https://pageofme.github.io/team1_prj301/images/aoSoMiKeNam.jpg'),
    (42, 'https://pageofme.github.io/team1_prj301/images/aoSoMiKeNamDen.jpg'),
    (42, 'https://pageofme.github.io/team1_prj301/images/aoSoMiKeNamTrang.jpg'),
    (43, 'https://pageofme.github.io/team1_prj301/images/aoSoMiKeNu.jpg'),
    (43, 'https://pageofme.github.io/team1_prj301/images/aoSoMiKeNuXanh.jpg'),
    (43, 'https://pageofme.github.io/team1_prj301/images/aoSoMiKeNuTrang.webp'),
    (44, 'https://pageofme.github.io/team1_prj301/images/aoChongNangNam.jpg'),
    (44, 'https://pageofme.github.io/team1_prj301/images/aoChongNangNamTrang.jpg'),
    (44, 'https://pageofme.github.io/team1_prj301/images/aoChongNangNamDen.webp'),
    (45, 'https://pageofme.github.io/team1_prj301/images/aoChongNangNu.jpg'),
    (45, 'https://pageofme.github.io/team1_prj301/images/aoChongNangNuDen.jpg'),
    (45, 'https://pageofme.github.io/team1_prj301/images/aoChongNangNuTrang.jpg');

-- Insert into colors
INSERT INTO colors (colorname, description)
VALUES 
    ('Đen', 'Màu đen'),
    ('Trắng', 'Màu trắng'),
    ('Xanh', 'Màu xanh');

-- Insert into sizes
INSERT INTO sizes (sizename, description)
VALUES 
    ('L', 'Size Large'),
    ('XL', 'Size Extra Large'),
    ('XXL', 'Size Double Extra Large');

-- Insert into productsizecolor
INSERT INTO productsizecolor (productid, sizeid, colorid)
SELECT p.productid, s.sizeid, c.colorid
FROM products p
CROSS JOIN sizes s
CROSS JOIN colors c;

-- Insert into cart
INSERT INTO cart (userid, createddate)
VALUES 
    (1, '2025-01-01'),
    (1, '2025-01-02'),
    (1, '2025-01-03'),
    (1, '2025-01-04'),
    (2, '2025-01-01'),
    (2, '2025-01-01'),
    (2, '2025-01-05'),
    (2, '2025-01-06'),
    (3, '2025-01-07'),
    (3, '2025-01-07'),
    (4, '2025-01-07'),
    (4, '2025-01-08'),
    (5, '2025-01-01'),
    (6, '2025-01-02'),
    (7, '2025-01-01'),
    (7, '2025-01-03'),
    (1, '2025-01-01'),
    (1, '2025-01-01'),
    (2, '2025-01-01'),
    (2, '2025-01-01'),
    (2, '2025-01-05'),
    (2, '2025-01-06'),
    (3, '2025-01-07'),
    (3, '2025-01-07'),
    (4, '2025-01-07'),
    (4, '2025-01-08'),
    (5, '2025-01-01'),
    (6, '2025-01-02'),
    (7, '2025-01-01'),
    (7, '2025-01-03'),
    (1, '2025-01-01'),
    (1, '2025-01-01'),
    (1, '2025-01-07'),
    (2, '2025-01-01'),
    (2, '2025-01-01'),
    (2, '2025-01-05'),
    (2, '2025-01-06'),
    (3, '2025-01-07'),
    (3, '2025-01-07'),
    (4, '2025-01-07'),
    (4, '2025-01-08'),
    (5, '2025-01-01'),
    (5, '2025-01-05'),
    (6, '2025-01-02'),
    (6, '2025-01-04'),
    (6, '2025-01-08'),
    (7, '2025-01-01'),
    (7, '2025-01-03'),
    (7, '2025-01-07'),
    (1, '2025-01-02'),
    (1, '2025-01-03'),
    (1, '2025-01-04'),
    (1, '2025-01-06'),
    (1, '2025-01-08'),
    (2, '2025-01-02'),
    (2, '2025-01-03'),
    (2, '2025-01-04'),
    (2, '2025-01-08'),
    (3, '2025-01-02'),
    (3, '2025-01-03'),
    (3, '2025-01-04'),
    (3, '2025-01-06'),
    (3, '2025-01-08'),
    (4, '2025-01-02'),
    (4, '2025-01-03'),
    (4, '2025-01-05'),
    (5, '2025-01-02'),
    (5, '2025-01-04'),
    (5, '2025-01-06'),
    (5, '2025-01-08');

-- Insert into CARTITEMS
INSERT INTO cartitems (cartid, productsizecolorid, quantity, addeddate)
VALUES
    (1, 1, 2, '2025-01-01'),
    (1, 2, 1, '2025-01-01'),
    (1, 3, 1, '2025-01-01'),
    (1, 4, 3, '2025-01-01'),
    (1, 5, 1, '2025-01-01'),
    (2, 6, 2, '2025-01-02'),
    (2, 7, 1, '2025-01-02'),
    (2, 8, 3, '2025-01-02'),
    (2, 9, 1, '2025-01-02'),
    (2, 10, 2, '2025-01-02'),
    (3, 11, 2, '2025-01-03'),
    (3, 12, 1, '2025-01-03'),
    (3, 13, 1, '2025-01-03'),
    (3, 14, 2, '2025-01-03'),
    (4, 15, 3, '2025-01-04'),
    (4, 16, 2, '2025-01-04'),
    (4, 17, 1, '2025-01-04'),
    (5, 18, 2, '2025-01-05'),
    (5, 19, 1, '2025-01-05'),
    (5, 20, 3, '2025-01-05'),
    (5, 21, 1, '2025-01-05'),
    (6, 22, 2, '2025-01-06'),
    (6, 23, 1, '2025-01-06'),
    (6, 24, 3, '2025-01-06'),
    (6, 25, 1, '2025-01-06'),
    (7, 26, 3, '2025-01-07'),
    (7, 27, 2, '2025-01-07'),
    (7, 28, 1, '2025-01-07'),
    (7, 29, 2, '2025-01-07'),
    (7, 30, 1, '2025-01-07'),
    (8, 1, 2, '2025-01-01'),
    (8, 2, 1, '2025-01-01'),
    (8, 3, 1, '2025-01-01'),
    (8, 4, 3, '2025-01-01'),
    (9, 6, 2, '2025-01-02'),
    (9, 7, 1, '2025-01-02'),
    (9, 8, 3, '2025-01-02'),
    (9, 9, 1, '2025-01-02'),
    (10, 11, 2, '2025-01-03'),
    (10, 12, 1, '2025-01-03'),
    (10, 13, 1, '2025-01-03'),
    (10, 14, 2, '2025-01-03'),
    (11, 15, 3, '2025-01-04'),
    (11, 16, 2, '2025-01-04'),
    (11, 17, 1, '2025-01-04'),
    (12, 18, 2, '2025-01-05'),
    (12, 19, 1, '2025-01-05'),
    (12, 20, 3, '2025-01-05'),
    (12, 21, 1, '2025-01-05'),
    (13, 22, 2, '2025-01-06'),
    (13, 23, 1, '2025-01-06'),
    (13, 24, 3, '2025-01-06'),
    (13, 25, 1, '2025-01-06'),
    (14, 26, 3, '2025-01-07'),
    (14, 27, 2, '2025-01-07'),
    (14, 28, 1, '2025-01-07'),
    (14, 29, 2, '2025-01-07'),
    (14, 30, 1, '2025-01-07'),
    (15, 1, 2, '2025-01-01'),
    (15, 2, 1, '2025-01-01'),
    (15, 3, 1, '2025-01-01'),
    (15, 4, 3, '2025-01-01'),
    (16, 6, 2, '2025-01-02'),
    (16, 7, 1, '2025-01-02'),
    (16, 8, 3, '2025-01-02'),
    (16, 9, 1, '2025-01-02'),
    (17, 11, 2, '2025-01-03'),
    (17, 12, 1, '2025-01-03'),
    (17, 13, 1, '2025-01-03'),
    (17, 14, 2, '2025-01-03'),
    (18, 15, 3, '2025-01-04'),
    (18, 16, 2, '2025-01-04'),
    (18, 17, 1, '2025-01-04'),
    (19, 18, 2, '2025-01-05'),
    (19, 19, 1, '2025-01-05'),
    (19, 20, 3, '2025-01-05'),
    (19, 21, 1, '2025-01-05'),
    (20, 22, 2, '2025-01-06'),
    (20, 23, 1, '2025-01-06'),
    (20, 24, 3, '2025-01-06'),
    (20, 25, 1, '2025-01-06'),
    (21, 26, 3, '2025-01-07'),
    (21, 27, 2, '2025-01-07'),
    (21, 28, 1, '2025-01-07'),
    (21, 29, 2, '2025-01-07'),
    (21, 30, 1, '2025-01-07'),
    (22, 1, 2, '2025-01-01'),
    (22, 2, 1, '2025-01-01'),
    (22, 3, 1, '2025-01-01'),
    (22, 4, 3, '2025-01-01'),
    (23, 6, 2, '2025-01-02'),
    (23, 7, 1, '2025-01-02'),
    (23, 8, 3, '2025-01-02'),
    (23, 9, 1, '2025-01-02'),
    (24, 11, 2, '2025-01-03'),
    (24, 12, 1, '2025-01-03'),
    (24, 13, 1, '2025-01-03'),
    (24, 14, 2, '2025-01-03'),
    (25, 15, 3, '2025-01-04'),
    (25, 16, 2, '2025-01-04'),
    (25, 17, 1, '2025-01-04'),
    (26, 18, 2, '2025-01-05'),
    (26, 19, 1, '2025-01-05'),
    (26, 20, 3, '2025-01-05'),
    (26, 21, 1, '2025-01-05'),
    (27, 22, 2, '2025-01-06'),
    (27, 23, 1, '2025-01-06'),
    (27, 24, 3, '2025-01-06'),
    (27, 25, 1, '2025-01-06'),
    (28, 26, 3, '2025-01-07'),
    (28, 27, 2, '2025-01-07'),
    (28, 28, 1, '2025-01-07'),
    (28, 29, 2, '2025-01-07'),
    (28, 30, 1, '2025-01-07'),
    (29, 1, 2, '2025-01-01'),
    (29, 2, 1, '2025-01-01'),
    (29, 3, 1, '2025-01-01'),
    (29, 4, 3, '2025-01-01'),
    (30, 6, 2, '2025-01-02'),
    (30, 7, 1, '2025-01-02'),
    (30, 8, 3, '2025-01-02'),
    (30, 9, 1, '2025-01-02'),
    (31, 11, 2, '2025-01-03'),
    (31, 12, 1, '2025-01-03'),
    (31, 13, 1, '2025-01-03'),
    (31, 14, 2, '2025-01-03'),
    (32, 15, 3, '2025-01-04'),
    (32, 16, 2, '2025-01-04'),
    (32, 17, 1, '2025-01-04'),
    (33, 18, 2, '2025-01-05'),
    (33, 19, 1, '2025-01-05'),
    (33, 20, 3, '2025-01-05'),
    (33, 21, 1, '2025-01-05'),
    (34, 22, 2, '2025-01-06'),
    (34, 23, 1, '2025-01-06'),
    (34, 24, 3, '2025-01-06'),
    (34, 25, 1, '2025-01-06'),
    (35, 26, 3, '2025-01-07'),
    (35, 27, 2, '2025-01-07'),
    (35, 28, 1, '2025-01-07'),
    (35, 29, 2, '2025-01-07'),
    (35, 30, 1, '2025-01-07'),
    (36, 1, 2, '2025-01-01'),
    (36, 2, 1, '2025-01-01'),
    (36, 3, 1, '2025-01-01'),
    (36, 4, 3, '2025-01-01'),
    (37, 6, 2, '2025-01-02'),
    (37, 7, 1, '2025-01-02'),
    (37, 8, 3, '2025-01-02'),
    (37, 9, 1, '2025-01-02'),
    (38, 11, 2, '2025-01-03'),
    (38, 12, 1, '2025-01-03'),
    (38, 13, 1, '2025-01-03'),
    (38, 14, 2, '2025-01-03'),
    (39, 15, 3, '2025-01-04'),
    (39, 16, 2, '2025-01-04'),
    (39, 17, 1, '2025-01-04'),
    (40, 18, 2, '2025-01-05'),
    (40, 19, 1, '2025-01-05'),
    (40, 20, 3, '2025-01-05'),
    (40, 21, 1, '2025-01-05'),
    (41, 22, 2, '2025-01-06'),
    (41, 23, 1, '2025-01-06'),
    (41, 24, 3, '2025-01-06'),
    (41, 25, 1, '2025-01-06'),
    (42, 26, 3, '2025-01-07'),
    (42, 27, 2, '2025-01-07'),
    (42, 28, 1, '2025-01-07'),
    (42, 29, 2, '2025-01-07'),
    (42, 30, 1, '2025-01-07'),
    (43, 1, 2, '2025-01-01'),
    (43, 2, 1, '2025-01-01'),
    (43, 3, 1, '2025-01-01'),
    (43, 4, 3, '2025-01-01'),
    (44, 6, 2, '2025-01-02'),
    (44, 7, 1, '2025-01-02'),
    (44, 8, 3, '2025-01-02'),
    (44, 9, 1, '2025-01-02'),
    (45, 11, 2, '2025-01-03'),
    (45, 12, 1, '2025-01-03'),
    (45, 13, 1, '2025-01-03'),
    (45, 14, 2, '2025-01-03'),
    (46, 15, 3, '2025-01-04'),
    (46, 16, 2, '2025-01-04'),
    (46, 17, 1, '2025-01-04'),
    (47, 18, 2, '2025-01-05'),
    (47, 19, 1, '2025-01-05'),
    (47, 20, 3, '2025-01-05'),
    (47, 21, 1, '2025-01-05'),
    (48, 22, 2, '2025-01-06'),
    (48, 23, 1, '2025-01-06'),
    (48, 24, 3, '2025-01-06'),
    (48, 25, 1, '2025-01-06'),
    (49, 26, 3, '2025-01-07'),
    (49, 27, 2, '2025-01-07'),
    (49, 28, 1, '2025-01-07'),
    (49, 29, 2, '2025-01-07'),
    (49, 30, 1, '2025-01-07'),
    (50, 1, 2, '2025-01-01'),
    (50, 2, 1, '2025-01-01'),
    (50, 3, 1, '2025-01-01'),
    (50, 4, 3, '2025-01-01'),
    (51, 6, 2, '2025-01-02'),
    (51, 7, 1, '2025-01-02'),
    (51, 8, 3, '2025-01-02'),
    (51, 9, 1, '2025-01-02'),
    (52, 11, 2, '2025-01-03'),
    (52, 12, 1, '2025-01-03'),
    (52, 13, 1, '2025-01-03'),
    (52, 14, 2, '2025-01-03'),
    (53, 15, 3, '2025-01-04'),
    (53, 16, 2, '2025-01-04'),
    (53, 17, 1, '2025-01-04'),
    (54, 18, 2, '2025-01-05'),
    (54, 19, 1, '2025-01-05'),
    (54, 20, 3, '2025-01-05'),
    (54, 21, 1, '2025-01-05'),
    (55, 22, 2, '2025-01-06'),
    (55, 23, 1, '2025-01-06'),
    (55, 24, 3, '2025-01-06'),
    (55, 25, 1, '2025-01-06'),
    (56, 26, 3, '2025-01-07'),
    (56, 27, 2, '2025-01-07'),
    (56, 28, 1, '2025-01-07'),
    (56, 29, 2, '2025-01-07'),
    (56, 30, 1, '2025-01-07'),
    (57, 1, 2, '2025-01-01'),
    (57, 2, 1, '2025-01-01'),
    (57, 3, 1, '2025-01-01'),
    (57, 4, 3, '2025-01-01'),
    (58, 6, 2, '2025-01-02'),
    (58, 7, 1, '2025-01-02'),
    (58, 8, 3, '2025-01-02'),
    (58, 9, 1, '2025-01-02'),
    (59, 11, 2, '2025-01-03'),
    (59, 12, 1, '2025-01-03'),
    (59, 13, 1, '2025-01-03'),
    (59, 14, 2, '2025-01-03'),
    (60, 15, 3, '2025-01-04'),
    (60, 16, 2, '2025-01-04'),
    (60, 17, 1, '2025-01-04'),
    (61, 18, 2, '2025-01-05'),
    (61, 19, 1, '2025-01-05'),
    (61, 20, 3, '2025-01-05'),
    (61, 21, 1, '2025-01-05'),
    (62, 22, 2, '2025-01-06'),
    (62, 23, 1, '2025-01-06'),
    (62, 24, 3, '2025-01-06'),
    (62, 25, 1, '2025-01-06'),
    (63, 26, 3, '2025-01-07'),
    (63, 27, 2, '2025-01-07'),
    (63, 28, 1, '2025-01-07'),
    (63, 29, 2, '2025-01-07'),
    (63, 30, 1, '2025-01-07'),
    (64, 1, 2, '2025-01-01'),
    (64, 2, 1, '2025-01-01'),
    (64, 3, 1, '2025-01-01'),
    (64, 4, 3, '2025-01-01'),
    (65, 6, 2, '2025-01-02'),
    (65, 7, 1, '2025-01-02'),
    (65, 8, 3, '2025-01-02'),
    (65, 9, 1, '2025-01-02'),
    (66, 11, 2, '2025-01-03'),
    (66, 12, 1, '2025-01-03'),
    (66, 13, 1, '2025-01-03'),
    (66, 14, 2, '2025-01-03'),
    (67, 15, 3, '2025-01-04'),
    (67, 16, 2, '2025-01-04'),
    (67, 17, 1, '2025-01-04'),
    (68, 18, 2, '2025-01-05'),
    (68, 19, 1, '2025-01-05'),
    (68, 20, 3, '2025-01-05'),
    (68, 21, 1, '2025-01-05'),
    (69, 22, 2, '2025-01-06'),
    (69, 23, 1, '2025-01-06'),
    (69, 24, 3, '2025-01-06'),
    (69, 25, 1, '2025-01-06');

-- Insert into PaymentMethods
INSERT INTO paymentmethods (methodname, description, isactive)
VALUES 
    ('COD', 'Cash on Delivery', TRUE),
    ('Credit Card', 'Payment via Credit Card', TRUE);

-- Insert into ORDERS
INSERT INTO orders (userid, orderdate, totalamount, paymentmethodid)
VALUES
    (1, '2025-02-01', NULL, 1),
    (1, '2025-02-02', NULL, 2),
    (2, '2025-02-03', NULL, 1),
    (2, '2025-02-04', NULL, 2),
    (3, '2025-02-05', NULL, 1),
    (3, '2025-02-06', NULL, 2),
    (4, '2025-02-07', NULL, 1),
    (4, '2025-02-08', NULL, 2),
    (5, '2025-02-01', NULL, 1),
    (5, '2025-02-02', NULL, 2),
    (6, '2025-02-03', NULL, 1),
    (6, '2025-02-04', NULL, 2),
    (7, '2025-02-05', NULL, 1),
    (7, '2025-02-06', NULL, 2),
    (1, '2025-02-07', NULL, 1),
    (1, '2025-02-08', NULL, 2),
    (2, '2025-02-01', NULL, 1),
    (2, '2025-02-02', NULL, 2),
    (3, '2025-02-03', NULL, 1),
    (3, '2025-02-04', NULL, 2),
    (4, '2025-02-05', NULL, 1),
    (4, '2025-02-06', NULL, 2),
    (5, '2025-02-07', NULL, 1),
    (5, '2025-02-08', NULL, 2),
    (6, '2025-02-01', NULL, 1),
    (6, '2025-02-02', NULL, 2),
    (7, '2025-02-03', NULL, 1),
    (7, '2025-02-04', NULL, 2),
    (1, '2025-02-05', NULL, 1),
    (1, '2025-02-06', NULL, 2),
    (2, '2025-02-07', NULL, 1),
    (2, '2025-02-08', NULL, 2),
    (3, '2025-02-01', NULL, 1),
    (3, '2025-02-02', NULL, 2),
    (4, '2025-02-03', NULL, 1),
    (4, '2025-02-04', NULL, 2),
    (5, '2025-02-05', NULL, 1),
    (5, '2025-02-06', NULL, 2);

-- Insert into ORDERSTATUS
INSERT INTO orderstatus (orderid, statusname)
VALUES
    (1, 'Pending'),
    (2, 'Processing'),
    (3, 'Shipped'),
    (4, 'Pending'),
    (5, 'Shipped'),
    (6, 'Processing'),
    (7, 'Pending'),
    (8, 'Shipped'),
    (9, 'Pending'),
    (10, 'Processing'),
    (11, 'Shipped'),
    (12, 'Pending'),
    (13, 'Shipped'),
    (14, 'Pending'),
    (15, 'Shipped'),
    (16, 'Processing'),
    (17, 'Pending'),
    (18, 'Shipped'),
    (19, 'Processing'),
    (20, 'Pending'),
    (21, 'Shipped'),
    (22, 'Pending'),
    (23, 'Processing'),
    (24, 'Shipped'),
    (25, 'Pending'),
    (26, 'Processing'),
    (27, 'Shipped'),
    (28, 'Pending'),
    (29, 'Shipped'),
    (30, 'Processing'),
    (31, 'Pending'),
    (32, 'Shipped'),
    (33, 'Processing'),
    (34, 'Pending'),
    (35, 'Shipped'),
    (36, 'Processing'),
    (37, 'Pending'),
    (38, 'Shipped');

-- Insert into ORDERDETAILS
INSERT INTO orderdetails (orderid, productsizecolorid, quantity, price)
SELECT o.orderid, ci.productsizecolorid, ci.quantity, ps.price
FROM orders o
JOIN cart c ON o.userid = c.userid
JOIN cartitems ci ON c.cartid = ci.cartid
JOIN productsizecolor ps ON ci.productsizecolorid = ps.productsizecolorid
WHERE o.orderid BETWEEN 1 AND 38;
