<?php
require_once("databaseconnect.php");

// Get POST values
$oldEmail   = trim($_POST['old_email'] ?? ''); // Email to identify the user
$newEmail   = trim($_POST['email'] ?? '');
$mobileno   = trim($_POST['mobileno'] ?? '');
$birthdate  = trim($_POST['birthdate'] ?? ''); // YYYY-MM-DD format

$response = array();

// Validation
if (empty($oldEmail) || empty($newEmail) || empty($mobileno) || empty($birthdate)) {
    $response['success'] = 0;
    $response['message'] = "All fields are required.";
    echo json_encode($response);
    exit;
}

// Check if old email exists
$checkQuery = "SELECT * FROM userdata WHERE email = ?";
$stmt = $connection->prepare($checkQuery);
$stmt->bind_param("s", $oldEmail);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows == 0) {
    $response['success'] = 0;
    $response['message'] = "User not found with old email.";
} else {
    // Update only email, mobile number, and birthdate
    $sql = "UPDATE userdata SET email = ?, mobileno = ?, birthdate = ? WHERE email = ?";
    $stmt = $connection->prepare($sql);
    $stmt->bind_param("ssss", $newEmail, $mobileno, $birthdate, $oldEmail);

    if ($stmt->execute()) {
        $response['success'] = 1;
        $response['message'] = "User updated successfully.";
    } else {
        $response['success'] = 0;
        $response['message'] = "Failed to update user.";
    }
}

echo json_encode($response);
?>
