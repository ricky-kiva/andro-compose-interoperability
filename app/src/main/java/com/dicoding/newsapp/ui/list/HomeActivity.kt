package com.dicoding.newsapp.ui.list

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.newsapp.R
import com.dicoding.newsapp.databinding.ActivityHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(
            binding.tabs, binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.home, R.string.bookmark)
    }
}

// Interoperability is the ability for system to share information with another information
// - it means that Jetpack Compose could be combined together with XML View

// 2 mechanism of migration:
// - Bottom-up: from smallest component (TextView, EditTest, ImageView, etc) to Compose (Text, TextField, Image, etc)
// - Top-down : from largest component (LinearLayout, RecyclerView, etc) to Compose (Column, LazyList, etc)

// 2 ways to combine Jetpack Compose with XML View:
// - Compose inside View
// - View inside Compose

// Compose Inside Activity:
// - `setContent` to add Jetpack Compose to Activity
// --- dependency used: implementation 'androidx.activity:activity-compose:$version'
/* add Compose to Activity:
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			HelloJetpackComposeTheme {
				   Greeting("Jetpack Compose")
			}
		}
	}
}

@Composable
fun Greeting(name: String) {
	Text(text = "Hello $name!")
}*/

// Compose inside XML:
// - `ComposeView/AbstractComposeView` to add Compose to XML
/* inside XML:
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.compose.ui.platform.ComposeView
        android:id="@+id/compose_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</LinearLayout>*/

/* inside Fragment:
class ExampleFragment : Fragment() {
	private var _binding: FragmentExampleBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentExampleBinding.inflate(inflater, container, false)
		val view = binding.root
		binding.composeView.apply {
			// Dispose Composition when view's LifecycleOwner is destroyed
			setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
			setContent {
				MaterialTheme {
					Text("Hello Compose!")
				}
			}
		}
		return view
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}*/

// Types of ViewCompositionStrategy:
// - DisposeOnDetachedFromWindowOrReleasedFromPool (default):
// --- erase element when it's not on Screen
// - DisposeOnViewTreeLifecycleDestroyed:
// --- follow parent's lifecycle
// - DisposeOnLifecycleDestroyed:
// --- element is disposed when LifeCycleOwner calling onDestroy()

// View inside Compose:
// - use `AndroidView` Composable Function to use View inside Composable
/* `factory` used for making View component. `update` used to renew UI according to state:
@Composable
private fun HtmlDescription(description: String) {
	val htmlDescription = remember(description) {
		HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
	}
	AndroidView(
		factory = { context ->
			TextView(context).apply { movementMethod = LinkMovementMethod.getInstance() }
		},
		update = {
			it.text = htmlDescription
		}
	)
}

@Preview
@Composable
private fun HtmlDescriptionPreview() {
	MaterialTheme {
		HtmlDescription("HTML<br><br>description")
	}
}
*/

// XML inside Compose:
// `AndroidViewBinding` to display whole XML inside Compose
/* ViewBinding need to be enabled for this:
@Composable
fun AndroidViewBindingExample() {
	AndroidViewBinding(ExampleLayoutBinding::inflate) {
		exampleView.setBackgroundColor(Color.GRAY)
	}
}*/

// Fragment inside Compose: another way to add Fragment inside Compose
/* add FragmentContainerView in XML with "name":
<androidx.fragment.app.FragmentContainerView
  xmlns:android="http://schemas.android.com/apk/res/android"
  android:id="@+id/fragment_container_view"
  android:layout_height="match_parent"
  android:layout_width="match_parent"
  android:name="com.example.MyFragment" />*/

/*call it on Composable Function:
@Composable
fun FragmentInComposeExample() {
	AndroidViewBinding(MyFragmentLayoutBinding::inflate) {
		val myFragment = fragmentContainerView.getFragment<MyFragment>()
		// ...
	}
}*/

// CompositionLocal: is a top level property to send data implicitly into Composable Function
// - implicit means able to use value from outside without define it as parameter
// - example of usage:
// --- the need of certain class from Android Framework (BroadcastReceiver, Service, Activity, etc)
// --- accessing Context to access data from resources (res)
// - example of CompositionLocal:
// --- LocalContext: get Context
// --- LocalConfiguration: get configuration (input mode, screen size, orientation, etc)
// --- LocalView: get View object

// use `.current` property to access current value of CompositionLocal
/*example:
@Composable
fun ToastGreetingButton(greeting: String) {
	val context = LocalContext.current
	Button(onClick = {
		Toast.makeText(context, greeting, Toast.LENGTH_SHORT).show()
	}) {
		Text("Greet")
	}
}*/