package cl.goodhealthy.cookify.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AuthTabs(
    selected: Int,                // 0 = login, 1 = register
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = cs.surfaceVariant,
        modifier = modifier
    ) {
        Row(modifier = Modifier.padding(6.dp)) {
            Pill(
                text = "Iniciar Sesión",
                selected = selected == 0,
                onClick = onLogin,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Pill(
                text = "Registrarse",
                selected = selected == 1,
                outlined = true,
                onClick = onRegister,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun Pill(
    text: String,
    selected: Boolean,
    outlined: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(16.dp)
    val colors = if (selected && !outlined) {
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = cs.onSurface
        )
    } else ButtonDefaults.buttonColors(
        containerColor = cs.surfaceVariant,
        contentColor = cs.onSurface
    )

    val border = if (outlined) BorderStroke(1.dp, cs.primary) else null

    Button(
        onClick = onClick,
        shape = shape,
        colors = colors,
        border = border,
        modifier = modifier.height(40.dp),
        contentPadding = PaddingValues(vertical = 0.dp)
    ) { Text(text) }
}
