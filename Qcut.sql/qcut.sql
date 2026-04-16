-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 18, 2025 at 09:51 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cut`
--

-- --------------------------------------------------------

--
-- Table structure for table `appoinment`
--

CREATE TABLE `appoinment` (
  `id` int(11) NOT NULL,
  `shopname` varchar(222) NOT NULL,
  `shopaddress` varchar(222) NOT NULL,
  `shopemail` varchar(222) NOT NULL,
  `finalname` varchar(222) NOT NULL,
  `finalemail` varchar(222) NOT NULL,
  `finaldate` varchar(222) NOT NULL,
  `finaltime` varchar(222) NOT NULL,
  `finalservice` varchar(222) NOT NULL,
  `totalamount` varchar(222) NOT NULL,
  `paymentMode` varchar(222) NOT NULL,
  `paymentStatus` varchar(2222) NOT NULL,
  `setno` varchar(222) NOT NULL,
  `statu` varchar(222) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `appoinment`
--

INSERT INTO `appoinment` (`id`, `shopname`, `shopaddress`, `shopemail`, `finalname`, `finalemail`, `finaldate`, `finaltime`, `finalservice`, `totalamount`, `paymentMode`, `paymentStatus`, `setno`, `statu`) VALUES
(15, 'Beauty Mark', 'Khulkhed,Akola,Maharashtra', 'parth@gmail.com', 'Parth Dahapute', 'parthdahaputedev@gmail.com', '18/9/2025', '04:03 PM', 'Haircut,Beard Trime,Facial,coloring,manicures', '1520', 'Online', 'Payment Done', '2', 'Confirm');

-- --------------------------------------------------------

--
-- Table structure for table `booking`
--

CREATE TABLE `booking` (
  `id` int(11) NOT NULL,
  `shopname` varchar(222) NOT NULL,
  `shopaddress` varchar(222) NOT NULL,
  `shopemail` varchar(222) NOT NULL,
  `finalname` varchar(222) NOT NULL,
  `finalemail` varchar(222) NOT NULL,
  `finaldate` varchar(222) NOT NULL,
  `finaltime` varchar(222) NOT NULL,
  `finalservice` varchar(222) NOT NULL,
  `totalamount` varchar(222) NOT NULL,
  `paymentMode` varchar(222) NOT NULL,
  `paymentStatus` varchar(222) NOT NULL,
  `setno` varchar(222) NOT NULL,
  `statu` varchar(222) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `image` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`id`, `name`, `image`) VALUES
(1, 'Women', 'women.jpg'),
(2, 'Men', 'men.jpg'),
(3, 'Kids', 'kids.jpg'),
(4, 'Unisex', 'unisex.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `fav`
--

CREATE TABLE `fav` (
  `id` int(11) NOT NULL,
  `namea` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `time` varchar(255) NOT NULL,
  `day` varchar(222) NOT NULL,
  `service` varchar(222) NOT NULL,
  `image` varchar(222) NOT NULL,
  `type` varchar(222) NOT NULL,
  `lattitude` varchar(222) NOT NULL,
  `logitude` varchar(222) NOT NULL,
  `shopemail` varchar(222) NOT NULL,
  `shopid` varchar(222) NOT NULL,
  `userEmail` varchar(222) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `fav`
--

INSERT INTO `fav` (`id`, `namea`, `address`, `time`, `day`, `service`, `image`, `type`, `lattitude`, `logitude`, `shopemail`, `shopid`, `userEmail`) VALUES
(1, 'Beauty Mark', 'Khulkhed,Akola,Maharashtra', '8 am to 9 pm', 'All Day', 'Haircut-100,Beard Trime-200,Facial-300,coloring-500,manicures-400', 's1.jpg', 'Unisex', 'null', 'null', 'parth@gmail.com', '1', 'parthdahaputedev@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `history`
--

CREATE TABLE `history` (
  `id` int(11) NOT NULL,
  `shopname` varchar(222) NOT NULL,
  `shopaddress` varchar(222) NOT NULL,
  `shopemail` varchar(222) NOT NULL,
  `finalname` varchar(222) NOT NULL,
  `finalemail` varchar(222) NOT NULL,
  `finaldate` varchar(222) NOT NULL,
  `finaltime` varchar(222) NOT NULL,
  `finalservice` varchar(222) NOT NULL,
  `totalamount` varchar(222) NOT NULL,
  `paymentMode` varchar(222) NOT NULL,
  `paymentStatus` varchar(222) NOT NULL,
  `Appoinmentstatus` varchar(222) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `history`
--

INSERT INTO `history` (`id`, `shopname`, `shopaddress`, `shopemail`, `finalname`, `finalemail`, `finaldate`, `finaltime`, `finalservice`, `totalamount`, `paymentMode`, `paymentStatus`, `Appoinmentstatus`) VALUES
(1, 'Beauty Mark', 'Khulkhed,Akola,Maharashtra', 'parth@gmail.com', 'Parth Dahapute', 'parthdahaputedev@gmail.com', '26/8/2025', '09:47 AM', 'Haircut,coloring,Beard Trime,manicures,Facial', '1520', 'Cash', 'Pending', 'Done'),
(2, 'Beauty Mark', 'Khulkhed,Akola,Maharashtra', 'parth@gmail.com', 'Parth Dahapute', 'parthdahaputedev@gmail.com', '18/9/2025', '06:16 PM', 'Haircut,Beard Trime', '320', 'Cash', 'Pending', 'Done'),
(3, 'Beauty Mark', 'Khulkhed,Akola,Maharashtra', 'parth@gmail.com', 'Parth Dahapute', 'parthdahaputedev@gmail.com', '18/9/2025', '07:32 PM', 'Haircut,Beard Trime', '320', 'Cash', 'Pending', 'Done');

-- --------------------------------------------------------

--
-- Table structure for table `imageslider`
--

CREATE TABLE `imageslider` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `image` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `imageslider`
--

INSERT INTO `imageslider` (`id`, `name`, `image`) VALUES
(1, 'img', 'img.jpg'),
(2, 'img1', 'img1.jpg'),
(3, 'img3', 'img3.jpg\r\n');

-- --------------------------------------------------------

--
-- Table structure for table `shop`
--

CREATE TABLE `shop` (
  `id` int(11) NOT NULL,
  `name` int(11) NOT NULL,
  `address` int(11) NOT NULL,
  `image` int(11) NOT NULL,
  `time` varchar(222) NOT NULL,
  `open days` varchar(222) NOT NULL,
  `service` varchar(222) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `shopdata`
--

CREATE TABLE `shopdata` (
  `id` int(11) NOT NULL,
  `namea` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `time` varchar(255) NOT NULL,
  `day` varchar(222) NOT NULL,
  `service` varchar(222) NOT NULL,
  `image` varchar(222) NOT NULL,
  `type` varchar(222) NOT NULL,
  `lattitude` varchar(222) NOT NULL,
  `logitude` varchar(222) NOT NULL,
  `shopemail` varchar(222) NOT NULL,
  `rateing` varchar(222) NOT NULL,
  `status` varchar(222) NOT NULL,
  `totalsets` varchar(222) NOT NULL,
  `booksets` varchar(222) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `shopdata`
--

INSERT INTO `shopdata` (`id`, `namea`, `address`, `time`, `day`, `service`, `image`, `type`, `lattitude`, `logitude`, `shopemail`, `rateing`, `status`, `totalsets`, `booksets`) VALUES
(1, 'Beauty Mark', 'Khulkhed,Akola,Maharashtra', '8 am to 9 pm', 'All Day', 'Haircut-100,Beard Trime-200,Facial-300,coloring-500,manicures-400', '1756294732428.jpeg', 'Unisex', '20.68144106998813', '77.02079594101461', 'parth@gmail.com', '4.5', 'Open', '3', ''),
(2, 'Salon De Style', 'Khulkhed,Akola,Maharashtra', '8 am to 9 pm', 'All Day', 'Haircut-100,Beard Trime-200,Facial-300,coloring-500,manicures-400', 's2.jpg', 'Unisex', '20.69008558058619', '77.01958090045963', 'parthda@gmail.com', '4.0', 'Open', '10', ''),
(6, 'vaga', 'gage', '4:52 PM to 4:52 PM', 'gsgs', 'Hiar-200', 'null', 'Unisex', '20.6757884', '77.016449', 'ram@gmail.com', '3.8', 'Open', '5', ''),
(7, 'gsgs', 'gsgsh', '5:04 PM to 5:05 PM', 'All day', 'Haircut-700', 'null', 'Unisex', '20.6758355', '77.0164402', 'kam@gmail.com', '3.8', 'Open', '7', ''),
(8, 'Salon By Hair', 'Kaulkhed Akola', '10:06 PM to 6:06 PM', 'All Day', 'Haircut-200,Coloring-300', '1758127081794.jpeg', 'Unisex', '20.8990006', '77.7637987', 'parthdahaputedev1@gmail.com', '4.0', 'Open', '10', '');

-- --------------------------------------------------------

--
-- Table structure for table `splashscreen`
--

CREATE TABLE `splashscreen` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `image` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `splashscreen`
--

INSERT INTO `splashscreen` (`id`, `name`, `image`) VALUES
(1, 'splash1', 'logo1.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `userdata`
--

CREATE TABLE `userdata` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `mobileno` varchar(255) NOT NULL,
  `email` varchar(222) NOT NULL,
  `address` varchar(222) NOT NULL,
  `password` varchar(222) NOT NULL,
  `image` varchar(222) NOT NULL,
  `userrole` varchar(22) NOT NULL,
  `birthdate` varchar(222) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `userdata`
--

INSERT INTO `userdata` (`id`, `name`, `mobileno`, `email`, `address`, `password`, `image`, `userrole`, `birthdate`) VALUES
(4, 'Parth Dahapute', '9322766871', 'parthdahaputedev@gmail.com', 'Akola', 'Parth@123', '1755820872176.jpeg', 'Customer', ''),
(7, 'Ram', '9322766872', 'parth@gmail.com', 'Akola', 'Pass@123', '1756162949249.jpeg', 'Salon Owner', ''),
(9, 'jd', '123466488', 'parthda@gmail.com', 'kdqigd', 'kjdqw', 'qksd', 'Salon Owner', ''),
(12, 'Parth Dahapute', '9322766873', 'parthdahaputedev1@gmail.com', 'Akola', 'Parth@321', '1758126964126.jpeg', 'Salon Owner', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `appoinment`
--
ALTER TABLE `appoinment`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `booking`
--
ALTER TABLE `booking`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `fav`
--
ALTER TABLE `fav`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `history`
--
ALTER TABLE `history`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `imageslider`
--
ALTER TABLE `imageslider`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `shopdata`
--
ALTER TABLE `shopdata`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `splashscreen`
--
ALTER TABLE `splashscreen`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `userdata`
--
ALTER TABLE `userdata`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `appoinment`
--
ALTER TABLE `appoinment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `booking`
--
ALTER TABLE `booking`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `fav`
--
ALTER TABLE `fav`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `history`
--
ALTER TABLE `history`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `imageslider`
--
ALTER TABLE `imageslider`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `shopdata`
--
ALTER TABLE `shopdata`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `splashscreen`
--
ALTER TABLE `splashscreen`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `userdata`
--
ALTER TABLE `userdata`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
