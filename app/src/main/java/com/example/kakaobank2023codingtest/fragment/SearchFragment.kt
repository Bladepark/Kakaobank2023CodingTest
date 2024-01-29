package com.example.kakaobank2023codingtest.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kakaobank2023codingtest.activity.MainActivity
import com.example.kakaobank2023codingtest.adapter.PostAdapter
import com.example.kakaobank2023codingtest.data.Document
import com.example.kakaobank2023codingtest.data.PostModel
import com.example.kakaobank2023codingtest.databinding.FragmentSearchBinding
import com.example.kakaobank2023codingtest.api.RetrofitInstance
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val postAdapter by lazy { PostAdapter() }

    private val _searchImages = MutableLiveData<List<Document>>()
    private val searchImages: LiveData<List<PostModel>> = _searchImages.switchMap {
        MutableLiveData(it.toPostModel())
    }

    companion object {
        fun newInstance() = SearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        setSearchListAdapter()
        setSearchProcess()
        setLastSearchQuery()

        searchImages.observe(viewLifecycleOwner) { images ->
            postAdapter.submitList(images.toList())
            postAdapter.itemClick = object : PostAdapter.ItemClick {
                override fun onClick(item: PostModel) {
                    val test =
                        (requireActivity() as MainActivity).favoriteListLiveData.value?.toMutableList() ?: mutableListOf()
                    val favorites = searchImages.value?.filter { list -> list.isFavorite }
                    if (favorites != null) {
                        test?.addAll(favorites.filterNot { test.contains(it) })
                        (requireActivity() as MainActivity).favoriteListLiveData.value = test
                    }
                }
            }
        }
    }

    private fun setSearchListAdapter() {
        binding.searchRecyclerView.apply {
            adapter = postAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun setSearchProcess() {
        binding.btnSearch.setOnClickListener {
            val query = binding.etSearch.text.toString()
            if (query.isNotBlank()) {
                fetchSearchImages(query)
                restoreLastSearchQuery(query)
                hideKeyboard()
            } else {
                Snackbar.make(requireView(), "검색창에 검색어를 입력하세요.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchSearchImages(query: String) {
        CoroutineScope(Dispatchers.Main).launch {
            runCatching {
                val documents = getSearchImages(query)
                val formattedDocuments = documents.map { document ->
                    document.copy(datetime = formatDateTime(document.datetime))
                }
                _searchImages.value = formattedDocuments
            }.onFailure {
                Log.e("fetchSearchImages", "fetchSearchImages(query) failed! : ${it.message}")
            }
        }
    }

    private suspend fun getSearchImages(query: String) = withContext(Dispatchers.IO) {
        RetrofitInstance.api.getSearchImages(query = query).documents
    }

    private fun formatDateTime(originalDateTime: String): String {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val targetFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = originalFormat.parse(originalDateTime)
        return date?.let { targetFormat.format(it) } ?: ""
    }

    private fun restoreLastSearchQuery(query: String) {
        val sharedPreferences =
            requireContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("lastSearchQuery", query)
        editor.apply()
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun setLastSearchQuery() {
        val sharedPreferences =
            requireContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val lastSearchQuery = sharedPreferences.getString("lastSearchQuery", "")
        binding.etSearch.setText(lastSearchQuery)
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

private fun List<Document>.toPostModel(): List<PostModel> {
    return this.map {
        PostModel(
            thumbnailUrl = it.thumbnailUrl.toUri(),
            siteName = it.displaySitename,
            postedTime = it.datetime,
            isFavorite = false
        )
    }
}