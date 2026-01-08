package com.example.firebasetest.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firebasetest.R
import com.example.firebasetest.modeldata.Siswa
import com.example.firebasetest.view.route.DestinasiDetail
import com.example.firebasetest.viewmodel.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailSiswaScreen(
    navigateToEditItem: (Long) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SiswaTopAppBar(
                title = stringResource(DestinasiDetail.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditItem(viewModel.idSiswa) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.edit_siswa),
                )
            }
        },
    ) { innerPadding ->
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        val uiStateDetail = viewModel.uiStateDetail

        BodyDetailSiswa(
            statusUIDetail = uiStateDetail,
            modifier = Modifier.padding(innerPadding),
            onDeleteClick = {
                deleteConfirmationRequired = true
            }
        )

        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    viewModel.hapusSatuSiswa()
                    deleteConfirmationRequired = false
                    navigateBack()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@Composable
fun BodyDetailSiswa(
    statusUIDetail: StatusUIDetail,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit
) {
    when {
        statusUIDetail is StatusUIDetail.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        statusUIDetail is StatusUIDetail.Success -> {
            val siswa = statusUIDetail.satusiswa
            if (siswa != null) {
                ItemDetailSiswa(
                    siswa = siswa,
                    modifier = modifier.fillMaxWidth(),
                    onDeleteClick = onDeleteClick
                )
            }
        }
        statusUIDetail is StatusUIDetail.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Gagal memuat data")
            }
        }
    }
}

@Composable
fun ItemDetailSiswa(
    modifier: Modifier = Modifier,
    siswa: Siswa,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = modifier.padding(top = dimensionResource(id = R.dimen.padding_large)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
        ) {
            ComponentDetailSiswa(
                judul = "ID",
                isinya = siswa.id.toString()
            )
            ComponentDetailSiswa(
                judul = "Nama",
                isinya = siswa.nama
            )
            ComponentDetailSiswa(
                judul = "Alamat",
                isinya = siswa.alamat
            )
            ComponentDetailSiswa(
                judul = "No Telepon",
                isinya = siswa.telpon
            )

            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)))

            Button(
                onClick = onDeleteClick,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Hapus")
            }
        }
    }
}

@Composable
fun ComponentDetailSiswa(
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
    ) {
        Text(
            text = "$judul :",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = isinya,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        }
    )
}