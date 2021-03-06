package com.selasarteam.selidikpasar.view.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.selasarteam.selidikpasar.R
import com.selasarteam.selidikpasar.databinding.ActivityDetailSummaryBinding
import com.selasarteam.selidikpasar.model.local.entity.NewsEntity

class DetailSummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailSummaryBinding
    private var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupData()
        setupAction()
    }

    private fun setupView() {
        binding = ActivityDetailSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.title_detail)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupData() {
        val data = intent.getParcelableExtra<NewsEntity>(EXTRA_DATA) as NewsEntity
        binding.apply {
            tvTitleDetail.text = data.title
            tvSummaryDetail.text = data.predictedSummary
            tvDateDetail.text = data.date
            (getString(R.string.source) + data.author).also { tvAuthorDetail.text = it }
            Glide.with(this@DetailSummaryActivity)
                .load(data.image)
                .fitCenter()
                .apply(
                    RequestOptions
                        .placeholderOf(R.drawable.ic_image_loading)
                        .error(R.drawable.ic_broken_image)
                ).into(ivPhotoDetail)
            url = data.url
        }
    }

    private fun setupAction() {
        binding.btnReadMore.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}