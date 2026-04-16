<?php
require_once("databaseconnect.php"); // your DB connection file

header('Content-Type: application/json; charset=UTF-8');

// Get POST value safely
$email = trim($_POST['email'] ?? '');

$response = array();

// Basic validation
if (empty($email)) {
    $response['success'] = 0;
    $response['message'] = "Email is required.";
    echo json_encode($response);
    exit;
}

// Prepare query to check if email exists
$sql = "SELECT id, namea, address, shopemail, status, totalsets, booksets 
        FROM shopdata 
        WHERE shopemail = ? 
        LIMIT 1";

$stmt = $connection->prepare($sql);
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $shop = $result->fetch_assoc();

    $response['success'] = 1;
    $response['message'] = "Email exists in shopdata.";
    $response['shopdata'] = array(
        'id'        => $shop['id'],
        'namea'     => $shop['namea'],
        'address'   => $shop['address'],
        'shopemail' => $shop['shopemail'],
        'status'    => $shop['status'],
        'totalsets' => $shop['totalsets'],
        'booksets'  => $shop['booksets']
    );
} else {
    $response['success'] = 0;
    $response['message'] = "Email not found in shopdata.";
}

echo json_encode($response);
?>
