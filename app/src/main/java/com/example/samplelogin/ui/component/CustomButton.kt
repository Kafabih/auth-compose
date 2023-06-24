package com.example.samplelogin.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.samplelogin.ui.theme.Purple40

@Composable
fun CustomButton(
    color: Color,
    text: String,
    isEnable: Boolean = true,
    modifier: Modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 24.dp),
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
             color, contentColor = Color.White
        ),
        enabled = isEnable
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}

@Preview
@Composable
private fun LemiButtonOrangePreview() {
    CustomButton(Purple40, "Login") {

    }
}