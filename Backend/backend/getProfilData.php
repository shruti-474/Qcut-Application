<?php
require_once("databaseconnect.php");

$email = trim($_POST['email'] ?? ''); // Get email from POST

$response = array();

if (empty($email)) {
    $response['success'] = 0;
    $response['message'] = "Email is required.";
    echo json_encode($response);
    exit;
}

// Query to fetch user data
$sql = "SELECT id, name, mobileno, email, address, image, userrole FROM userdata WHERE email = ?";
$stmt = $connection->prepare($sql);
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $userData = $result->fetch_assoc();
    $response['success'] = 1;
    $response['userdata'] = $userData;
} else {
    $response['success'] = 0;
    $response['message'] = "User not found.";
}

echo json_encode($response);
?>
