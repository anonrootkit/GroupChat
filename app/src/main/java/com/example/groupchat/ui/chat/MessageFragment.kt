package com.example.groupchat.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.groupchat.databinding.FragmentMessageBinding
import com.example.groupchat.model.MessageReceived
import com.example.groupchat.model.MessageSend

class MessageFragment : Fragment() {

    private lateinit var viewModel: MessageViewModel
    private lateinit var binding : FragmentMessageBinding

    private val messageAdapter : MessageAdapter by lazy {
        MessageAdapter(layoutInflater,
                onItemLongClick = { message ->
                    showAlertBox(message)
                }
            )
    }

    private fun showAlertBox(message : MessageReceived) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Delete message ?")
        alertDialogBuilder.setMessage(message.message)

        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            viewModel.deleteMessageFromFirebase(messageId = message.id)
        }

        alertDialogBuilder.setNegativeButton("Cancel") { _, _ -> }

        alertDialogBuilder.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessageBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, MessageViewModel.Factory()).get(MessageViewModel::class.java)
        viewModel.getMessagesFromDB()

        viewModel.isMessageSent.observe(viewLifecycleOwner) { isMessageSent ->
            if (isMessageSent == true){
                binding.messageBox.setText("")
                viewModel.messageSent()
            }
        }

        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            if (!messages.isNullOrEmpty()){
                messageAdapter.submitList(messages.toList())
                binding.messegeList.scrollToPosition(messageAdapter.itemCount - 1)
            }else{
                messageAdapter.submitList(null)
            }
        }

        binding.messegeList.apply {
            adapter = messageAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }

        binding.sendButton.setOnClickListener {
            val name: String = binding.nameBox.text.toString().trim()
            val message: String = binding.messageBox.text.toString().trim()

            val formattedMessage: MessageSend =
                if (name.isBlank())
                    MessageSend(message = message)
                else
                    MessageSend(name, message)

            viewModel.uploadMessageOnDB(formattedMessage)
        }

    }


}
