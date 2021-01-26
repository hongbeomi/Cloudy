package github.hongbeomi.cloudy

import android.os.Build
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import github.hongbeomi.cloudy.databinding.ActivityMainBinding
import github.hongbeomi.library.Cloudy

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cloud = Cloudy
                .with(this)
                .from(binding.imageViewMainSample)
                .into(binding.textViewMain)

        binding.switchMainBlur.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> cloud.blur()
                false -> cloud.clear()
            }
        }
        binding.seekBarMainBlur.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                cloud.radius(progress.toFloat() / 4f)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.scrollViewMain.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                cloud.onVerticalScroll(scrollY)
            }
        }
    }

}