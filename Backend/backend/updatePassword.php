<?php
require_once("databaseconnect.php");

// Get POST values
$emailid     = trim($_POST['email'] ?? '');
$newpassword = trim($_POST['password'] ?? ''); // Keep plain or hash
$response    = array();

// Basic validation
if (empty($emailid) || empty($newpassword)) {
    $response['success'] = 0;
    $response['message'] = "Email and password are required.";
    echo json_encode($response);
    exit;
}

// Check if email exists
$checkQuery = "SELECT * FROM userdata WHERE email = ?";
$stmt = $connection->prepare($checkQuery);
$stmt->bind_param("s", $emailid);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 0) {
    $response['success'] = 0;
    $response['message'] = "Email not found.";
} else {
    // Update password (You should ideally hash it)
    $updateQuery = "UPDATE userdata SET password = ? WHERE email = ?";
    $stmt = $connection->prepare($updateQuery);
    $stmt->bind_param("ss", $newpassword, $emailid);

    if ($stmt->execute()) {
        $response['success'] = 1;
        $response['message'] = "Password updated successfully.";
    } else {
        $response['success'] = 0;
        $response['message'] = "Failed to update password.";
    }
}

echo json_encode($response);
?>
