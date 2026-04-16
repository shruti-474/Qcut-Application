<?php
require_once("databaseconnect.php");

// Get 'shopname' from POST request
$shopname = $_POST['shopname'] ?? '';

$response = array();

if (!empty($shopname)) {
    // Escape to prevent SQL injection
    $shopname = mysqli_real_escape_string($connection, $shopname);

    // Fetch ALL sets for that shop
    $sql = "SELECT sets FROM appoinment WHERE shopname='$shopname'";
    $result = mysqli_query($connection, $sql);

    if ($result && mysqli_num_rows($result) > 0) {
        $setsArray = array();

        while ($row = mysqli_fetch_assoc($result)) {
            $setsArray[] = $row['sets'];  // collect all sets
        }

        $response['status'] = "success";
        $response['shopname'] = $shopname;
        $response['sets'] = $setsArray;  // return as array
    } else {
        $response['status'] = "error";
        $response['message'] = "No shop found with name $shopname";
    }
} else {
    $response['status'] = "error";
    $response['message'] = "Shop name is required";
}

// Return JSON
header('Content-Type: application/json; charset=UTF-8');
echo json_encode($response);
?>
