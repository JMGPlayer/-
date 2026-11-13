package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun LoginScreen(
    isAuthenticating: Boolean,
    loginError: String?,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onLoginSubmit: (userId: String, password: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var userId by remember { mutableStateOf("minjun.kim@hyundai.com") }
    var password by remember { mutableStateOf("hyundai2026!") }
    var passwordVisible by remember { mutableStateOf(false) }
    var keepSignedIn by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CleanWhite)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Brand & Theme Switch Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = ChipSensorBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlueBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "HYUNDAI BlueMembers 연동",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = ChipSensorText
                        )
                    }
                }

                // Quick Dark/Light Mode Switch
                Surface(
                    onClick = { onToggleDarkMode(!isDarkMode) },
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceLight,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                    modifier = Modifier.testTag("login_dark_mode_toggle")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (isDarkMode) "라이트 모드" else "다크 모드",
                            tint = PrimaryNavy,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (isDarkMode) "라이트" else "다크",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Hyundai Connected Car Hero Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(BrandDarkNavy, BrandNavyContainer, PrimaryBlue.copy(alpha = 0.85f))
                        )
                    )
                    .padding(22.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(AlwaysWhite.copy(alpha = 0.16f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = "현대자동차 커넥티드 카 아이콘",
                                tint = AlwaysWhite,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "HYUNDAI CONNECTED",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AlwaysWhite.copy(alpha = 0.78f),
                                letterSpacing = 1.2.sp
                            )
                            Text(
                                text = "Car Value Coach",
                                fontSize = 23.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AlwaysWhite
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "내 현대자동차 센서 데이터와 공식 블루핸즈 정비 이력을 연결하여 주행 코칭과 투명 가치 리포트를 관리하세요.",
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = AlwaysWhite.copy(alpha = 0.88f)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(AlwaysWhite.copy(alpha = 0.12f))
                            .padding(horizontal = 12.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = null,
                                tint = AccentTeal,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "등록 차고: 쏘나타 디 엣지 외 2대",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AlwaysWhite
                            )
                        }
                        Text(
                            text = "김민준님",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentTeal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Login Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfacePureWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Column {
                        Text(
                            text = "현대자동차 계정 로그인",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "블루멤버스 / Pleos Connect 통합 아이디로 로그인합니다",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    // Active preset info pill
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(PrimaryBlueSubtle)
                            .border(1.dp, PrimaryBlueBorder, RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "현재 이용자 김민준님의 현대자동차 계정이 자동 입력되었습니다.",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryNavy,
                            lineHeight = 16.sp
                        )
                    }

                    // User ID Field
                    OutlinedTextField(
                        value = userId,
                        onValueChange = { userId = it },
                        label = { Text("현대자동차 아이디 (이메일)") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = PrimaryBlue
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_id_input")
                    )

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("비밀번호") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = PrimaryBlue
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible },
                                modifier = Modifier.testTag("toggle_password_visibility")
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (passwordVisible) "비밀번호 숨기기" else "비밀번호 보기",
                                    tint = TextSecondary
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (!isAuthenticating) {
                                    onLoginSubmit(userId, password)
                                }
                            }
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_password_input")
                    )

                    if (!loginError.isNullOrBlank()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(AlertRedLight)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = AlertRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = loginError,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AlertRed
                            )
                        }
                    }

                    // Keep signed in checkbox
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { keepSignedIn = !keepSignedIn }
                            .padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = keepSignedIn,
                            onCheckedChange = { keepSignedIn = it }
                        )
                        Text(
                            text = "로그인 상태 유지 및 차량 센서 자동 연동",
                            fontSize = 12.5.sp,
                            color = TextSecondary
                        )
                    }

                    // Login Submit Button
                    Button(
                        onClick = { onLoginSubmit(userId, password) },
                        enabled = !isAuthenticating,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("login_submit_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BrandButtonContainer,
                            contentColor = AlwaysWhite
                        )
                    ) {
                        if (isAuthenticating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = AlwaysWhite,
                                strokeWidth = 2.4.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "현대자동차 계정 인증 및 차량 연동 중...",
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = AlwaysWhite
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Login,
                                contentDescription = null,
                                tint = AlwaysWhite,
                                modifier = Modifier.size(19.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "김민준 계정으로 로그인",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = AlwaysWhite
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer Security Notice
            Text(
                text = "본 인증 화면은 김민준님의 현대자동차 블루멤버스 & Vehicle SDK 차량 연동 시연 환경입니다.",
                fontSize = 11.5.sp,
                color = TextTertiary,
                lineHeight = 16.sp
            )
        }
    }
}
