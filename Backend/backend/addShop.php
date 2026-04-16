<?php
require_once("databaseconnect.php");

// Collect POST parameters safely (with default empty string)
$namea      = $_POST['namea'] ?? '';
$address    = $_POST['address'] ?? '';
$time       = $_POST['time'] ?? '';
$day        = $_POST['day'] ?? '';
$service    = $_POST['service'] ?? '';
$image      = $_POST['image'] ?? '';
$type       = $_POST['type'] ?? '';
$lattitude  = $_POST['lattitude'] ?? '';
$logitude   = $_POST['logitude'] ?? '';
$shopEmail  = $_POST['shopemail'] ?? '';
$rating  = $_POST['rating'] ?? '';

// First check if shopemail already exists
$checkQuery = "SELECT id FROM shopdata WHERE shopemail = '$shopEmail' LIMIT 1";
$checkResult = mysqli_query($connection, $checkQuery);

if (mysqli_num_rows($checkResult) > 0) {
    // Shop already exists
    $respond['success'] = 0;
    $respond['message'] = "Shop with this email already exists";
} else {
    // Insert new shop
    $query = "INSERT INTO shopdata 
                (namea, address, time, day, service, image, type, lattitude, logitude, shopemail,rateing) 
              VALUES 
                ('$namea', '$address', '$time', '$day', '$service', '$image', '$type', '$lattitude', '$logitude', '$shopEmail', '$rating')";
    
    $result = mysqli_query($connection, $query);

    if ($result) {
        $respond['success'] = 1;
        $respond['message'] = "Shop inserted successfully";
    } else {
        $respond['success'] = 0;
        $respond['message'] = "Failed to insert: " . mysqli_error($connection);
    }
}

echo json_encode($respond);
?>
