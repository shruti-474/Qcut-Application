<?php
require_once("databaseconnect.php");

// Get POST values safely
$name       = trim($_POST['namea'] ?? '');
$address    = trim($_POST['address'] ?? '');
$time       = trim($_POST['time'] ?? '');   // Example: 8 AM to 9 PM
$day        = trim($_POST['day'] ?? '');
$service    = trim($_POST['service'] ?? '');
$image      = trim($_POST['image'] ?? '');  // store image filename or URL
$type       = trim($_POST['type'] ?? '');
$latitude   = trim($_POST['lattitude'] ?? '');
$longitude  = trim($_POST['logitude'] ?? '');
$shopemail  = trim($_POST['shopemail'] ?? '');

$response = array();

// Basic validation
if (empty($name) || empty($address) || empty($time) || empty($day) || empty($service) 
    || empty($type) || empty($latitude) || empty($longitude) || empty($shopemail)) {
    
    $response['success'] = 0;
    $response['message'] = "All required fields must be filled.";
    echo json_encode($response);
    exit;
}

// Check if email already exists in shopdata
$checkQuery = "SELECT * FROM shopdata WHERE shopemail = ?";
$stmt = $connection->prepare($checkQuery);
$stmt->bind_param("s", $shopemail);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    // Email already registered → Only one shop per user
    $response['success'] = 0;
    $response['message'] = "This email already registered a shop. Only one shop per user allowed.";
} else {
    // Insert new shop
    $sql = "INSERT INTO shopdata (namea, address, time, day, service, image, type, lattitude, logitude, shopemail) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    $stmt = $connection->prepare($sql);
    $stmt->bind_param("ssssssssss", $name, $address, $time, $day, $service, $image, $type, $latitude, $longitude, $shopemail);

    if ($stmt->execute()) {
        $response['success'] = 1;
        $response['message'] = "Shop registration successful.";
    } else {
        $response['success'] = 0;
        $response['message'] = "Failed to register shop.";
    }
}

echo json_encode($response);
?>
