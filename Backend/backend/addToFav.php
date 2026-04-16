<?php
require_once("databaseconnect.php");

// Get POST data safely, fallback to empty strings
$namea      = $_POST['namea'] ?? '';
$address    = $_POST['address'] ?? '';
$time       = $_POST['time'] ?? '';
$day        = $_POST['day'] ?? '';
$service    = $_POST['service'] ?? '';
$image      = $_POST['image'] ?? '';
$type       = $_POST['type'] ?? '';
$lattitude  = $_POST['lattitude'] ?? '';
$logitude   = $_POST['logitude'] ?? '';
$shopemail  = $_POST['shopemail'] ?? '';
$shopid     = $_POST['shopid'] ?? '';
$userEmail  = $_POST['userEmail'] ?? '';

// First, check if this shop is already in favorites for this user
$checkQuery = "SELECT * FROM fav WHERE shopid='$shopid' AND userEmail='$userEmail'";
$checkResult = mysqli_query($connection, $checkQuery);

if(mysqli_num_rows($checkResult) > 0){
    // Shop already in favorites
    $respond['success'] = 0;
    $respond['message'] = "Shop is already in favorites";
} else {
    // Prepare the INSERT query
    $query = "INSERT INTO fav 
                (namea, address, time, day, service, image, type, lattitude, logitude, shopemail, shopid, userEmail)
              VALUES 
                ('$namea', '$address', '$time', '$day', '$service', '$image', '$type', '$lattitude', '$logitude', '$shopemail', '$shopid', '$userEmail')";

    $result = mysqli_query($connection, $query);

    if ($result) {
        $respond['success'] = 1;
        $respond['message'] = "Added to favorites successfully";
    } else {
        $respond['success'] = 0;
        $respond['message'] = "Database error: " . mysqli_error($connection);
    }
}

// Return JSON response
echo json_encode($respond);
?>
