package com.example.triqui

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import com.example.triqui.ui.theme.TriquiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TriquiTheme {
                TriquiGame()
            }
        }
    }
}

@Composable
fun TriquiGame(modifier: Modifier = Modifier) {
    val context = LocalContext.current // Obtener el contexto
    var board by remember { mutableStateOf(List(3) { MutableList(3) { "" } }) }
    var isXTurn by remember { mutableStateOf(true) }
    var winner by remember { mutableStateOf<String?>(null) }
    var isDraw by remember { mutableStateOf(false) }
    var difficultyLevel by remember { mutableStateOf("Easy") }

    fun resetGame() {
        board = List(3) { MutableList(3) { "" } }
        isXTurn = true
        winner = null
        isDraw = false
    }

    fun checkWinner(): String? {
        for (i in 0..2) {
            if (board[i][0] != "" && board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
                return board[i][0]
            }
            if (board[0][i] != "" && board[0][i] == board[1][i] && board[0][i] == board[2][i]) {
                return board[0][i]
            }
        }
        if (board[0][0] != "" && board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
            return board[0][0]
        }
        if (board[0][2] != "" && board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
            return board[0][2]
        }
        return null
    }

    fun checkDraw(): Boolean {
        for (row in board) {
            for (cell in row) {
                if (cell == "") {
                    return false
                }
            }
        }
        return winner == null
    }

    winner = checkWinner()

    if (winner == null) {
        isDraw = checkDraw()
    }

    // Detectar la orientación
    val isPortrait = LocalConfiguration.current.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT

    if (isPortrait) {
        // Layout para orientación vertical
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when {
                winner != null -> {
                    Text("Jugador $winner gana!", style = MaterialTheme.typography.bodyLarge)
                    Button(onClick = { resetGame() }) {
                        Text("Nuevo Juego")
                    }
                }
                isDraw -> {
                    Text("¡Empate!", style = MaterialTheme.typography.bodyLarge)
                    Button(onClick = { resetGame() }) {
                        Text("Nuevo Juego")
                    }
                }
                else -> {
                    Text("Turno de: ${if (isXTurn) "X" else "O"}", style = MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tablero
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                for (row in 0 until 3) {
                    Row(horizontalArrangement = Arrangement.Center) {
                        for (col in 0 until 3) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(4.dp)
                                    .background(Color.Gray)
                                    .clickable {
                                        if (board[row][col].isEmpty() && winner == null && !isDraw) {
                                            board[row][col] = if (isXTurn) "X" else "O"
                                            isXTurn = !isXTurn
                                            winner = checkWinner()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                BasicText(
                                    text = board[row][col],
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

            // Botones
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { resetGame() }) {
                Text("Nuevo Juego")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                difficultyLevel = when (difficultyLevel) {
                    "Easy" -> "Harder"
                    "Harder" -> "Expert"
                    else -> "Easy"
                }
            }) {
                Text("Dificultad: $difficultyLevel")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                Toast.makeText(context, "Saliendo...", Toast.LENGTH_SHORT).show()
                (context as? Activity)?.finish() // Cerrar la actividad
            }) {
                Text("Salir")
            }
        }
    } else {
        // Layout para orientación horizontal
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Tablero en la izquierda
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
            ) {
                when {
                    winner != null -> {
                        Text("Jugador $winner gana!", style = MaterialTheme.typography.bodyLarge)
                        Button(onClick = { resetGame() }) {
                            Text("Nuevo Juego")
                        }
                    }
                    isDraw -> {
                        Text("¡Empate!", style = MaterialTheme.typography.bodyLarge)
                        Button(onClick = { resetGame() }) {
                            Text("Nuevo Juego")
                        }
                    }
                    else -> {
                        Text("Turno de: ${if (isXTurn) "X" else "O"}", style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tablero
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    for (row in 0 until 3) {
                        Row(horizontalArrangement = Arrangement.Center) {
                            for (col in 0 until 3) {
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(4.dp)
                                        .background(Color.Gray)
                                        .clickable {
                                            if (board[row][col].isEmpty() && winner == null && !isDraw) {
                                                board[row][col] = if (isXTurn) "X" else "O"
                                                isXTurn = !isXTurn
                                                winner = checkWinner()
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    BasicText(
                                        text = board[row][col],
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Botones en la derecha
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxHeight()
            ) {
                Button(onClick = { resetGame() }) {
                    Text("Nuevo Juego")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    difficultyLevel = when (difficultyLevel) {
                        "Easy" -> "Harder"
                        "Harder" -> "Expert"
                        else -> "Easy"
                    }
                }) {
                    Text("Dificultad: $difficultyLevel")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    Toast.makeText(context, "Saliendo...", Toast.LENGTH_SHORT).show()
                    (context as? Activity)?.finish() // Cerrar la actividad
                }) {
                    Text("Salir")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TriquiGamePreview() {
    TriquiTheme {
        TriquiGame()
    }
}
