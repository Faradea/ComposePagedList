package com.example.composetutorial

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
//import androidx.compose.foundation.lazy.items
import androidx.paging.compose.items

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // просто пример как использовать состояния
//            val expanded by remember { mutableStateOf(false) }
//            rememberSaveable если нужно пережить configuration changes и даже смерть приложения
//            ElevatedButton(
//                onClick = { expanded = !expanded },
//            ) {
//                Text(if (expanded.value) "Show less" else "Show more")
//            }
            ArticlesListScreen()
        }
    }
}

@Composable
fun ArticlesListScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel()
) {
    Column() {
        TopAppBar() {
            Text(text = stringResource(R.string.articles_screen_title))
        }
//                Row() {
//                    Icon(Icons.Filled.ArrowBack, null)
//                    TopAppBar() {
//                        Text("All Articles")
//                    }
//                }
        ComposeTutorialTheme {
            ArticlesList(articles = mainViewModel.getArticlesPaged())
        }
    }
}

@Composable
fun ArticleCard(
    title: String,
    desc: String,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
) {
    Column(modifier = Modifier
        .clickable {
            onClick.invoke(desc)
        }
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            elevation = 1.dp
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RectangleShape)
                    .height(100.dp)
                    .fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle2
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = desc,
            style = MaterialTheme.typography.body2
        )
    }
}

@Composable
fun ArticlesList(
    articles: LiveData<PagingData<Article>>,
    modifier: Modifier = Modifier
) {
    // ToDo убрать .asFlow()
    val articlesListItems: LazyPagingItems<Article> = articles.asFlow().collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier.padding(horizontal = 24.dp),
        contentPadding = PaddingValues(
            vertical = 24.dp
        ),
         verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(articlesListItems) { article ->
            article?.let {
                ArticleCard(article.title, article.desc) {
                    Log.d("TAG", "item is clicked, with desc: $it")
                }
            }
        }

        articlesListItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    //You can add modifier to manage load state when first time response page is loading
                }
                loadState.refresh is LoadState.Error -> {
                    item {
                        ErrorState(text = "Something wrong with initial load")
                    }
                }
                loadState.append is LoadState.Loading -> {
                    //You can add modifier to manage load state when next response page is loading
                }
                loadState.append is LoadState.Error -> {
                    val error = loadState.append as LoadState.Error
                    // You can use modifier to show error message
                    item {
                        ErrorState(text = error.error.message ?: "Something wrong")
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorState(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier.padding(24.dp),
        style = MaterialTheme.typography.caption
    )
}


//@Preview(name = "Light Mode", showBackground = true)
//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    showBackground = true,
//    name = "Dark Mode"
//)
//@Composable
//fun PreviewMessageCard() {
//    ComposeTutorialTheme {
//        ArticlesList(articles = getArticlesList())
//    }
//
//}