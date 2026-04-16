<?php
require_once("databaseconnect.php");

// Get POST values safely
$login_id    = trim($_POST['login_id'] ?? ''); // Can be email or mobile
$userpassword = trim($_POST['password'] ?? ''); // Plain text

$response = array();

// Basic validation
if (empty($login_id) || empty($userpassword)) {
    $response['success'] = 0;
    $response['message'] = "Email/Mobile and password are required.";
    echo json_encode($response);
    exit;
}

// Check if user exists with email or mobile and password
$sql = "SELECT * FROM userdata 
        WHERE (email = ? OR mobileno = ?) AND password = ?";
$stmt = $connection->prepare($sql);
$stmt->bind_param("sss", $login_id, $login_id, $userpassword);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $user = $result->fetch_assoc();
    
    $response['success'] = 1;
    $response['message'] = "Login successful.";
    $response['userdata'] = array(
        'id'       => $user['id'],
        'name'     => $user['name'],
        'mobileno' => $user['mobileno'],
        'email'    => $user['email'],
        'address'  => $user['address'],
        'image'    => $user['image'],
        'userrole' => $user['userrole']
    );
} else {
    $response['success'] = 0;
    $response['message'] = "Invalid email/mobile or password.";
}

echo json_encode($response);
?>
