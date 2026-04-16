<?php
require_once("databaseconnect.php");

// Get POST values safely
$name        = trim($_POST['name'] ?? '');
$emailid     = trim($_POST['email'] ?? '');
$address     = trim($_POST['address'] ?? '');
$mobileno    = trim($_POST['mobileno'] ?? '');
$userpassword= trim($_POST['password'] ?? ''); // Keep as plain text
$image       = trim($_POST['image'] ?? ''); // Store image filename or URL
$userrole    = trim($_POST['userrole'] ?? 'user'); // Default role: 'user'

$response = array();

// Basic validation
if (empty($name) || empty($emailid) || empty($mobileno) || empty($userpassword)) {
    $response['success'] = 0;
    $response['message'] = "All required fields must be filled.";
    echo json_encode($response);
    exit;
}

// Check if email or mobile number already exists
$checkQuery = "SELECT * FROM userdata WHERE email = ? OR mobileno = ?";
$stmt = $connection->prepare($checkQuery);
$stmt->bind_param("ss", $emailid, $mobileno); // ✅ 2 placeholders, 2 params
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $response['success'] = 0;
    $response['message'] = "User already exists with the same email or mobile number.";
} else {
    // Insert new user without password hashing
    $sql = "INSERT INTO userdata (name, mobileno, email, address, password, image, userrole) 
            VALUES (?, ?, ?, ?, ?, ?, ?)";
    $stmt = $connection->prepare($sql);
    $stmt->bind_param("sssssss", $name, $mobileno, $emailid, $address, $userpassword, $image, $userrole); // ✅ 7 placeholders, 7 params

    if ($stmt->execute()) {
        $response['success'] = 1;
        $response['message'] = "Registration successful.";
    } else {
        $response['success'] = 0;
        $response['message'] = "Failed to register user.";
    }
}

echo json_encode($response);
?>
