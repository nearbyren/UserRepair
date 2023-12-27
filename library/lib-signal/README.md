
LiveBus.get(Constants.LK_TEST1_POST, String::class.java).observeForever(viewModel.foreverObserver)

var foreverObserver = object : Observer<String> {
        override fun onChanged(t: String?) {
            foreverObserveData.value = "Forever订阅：${t}"
        }
 }

