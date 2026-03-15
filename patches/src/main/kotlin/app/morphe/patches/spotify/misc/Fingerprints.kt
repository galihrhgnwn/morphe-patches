@file:Suppress("CONTEXT_RECEIVERS_DEPRECATED")

package app.morphe.patches.spotify.misc

import app.revanced.patcher.fingerprint.BytecodePatchContext
import app.revanced.patcher.fingerprint.fingerprint
import app.revanced.util.getReference
import app.revanced.util.indexOfFirstInstruction
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.iface.reference.TypeReference

context(BytecodePatchContext)
internal val accountAttributeFingerprint get() = fingerprint {
    custom { _, classDef -> classDef.type == "Lcom/spotify/remoteconfig/internal/AccountAttribute;" }
}

context(BytecodePatchContext)
internal val productStateProtoGetMapFingerprint get() = fingerprint {
    returns("Ljava/util/Map;")
    custom { _, classDef -> classDef.type == "Lcom/spotify/remoteconfig/internal/ProductStateProto;" }
}

internal val buildQueryParametersFingerprint = fingerprint {
    strings("trackRows", "device_type:tablet")
}

internal val contextMenuViewModelClassFingerprint = fingerprint {
    strings("ContextMenuViewModel(header=")
}
