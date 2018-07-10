.class Lshubham/intern/MainActivity$2;
.super Ljava/lang/Object;
.source "MainActivity.java"

# interfaces
.implements Landroid/view/View$OnClickListener;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lshubham/intern/MainActivity;->onCreate(Landroid/os/Bundle;)V
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = null
.end annotation


# instance fields
.field final synthetic this$0:Lshubham/intern/MainActivity;


# direct methods
.method constructor <init>(Lshubham/intern/MainActivity;)V
    .locals 0
    .param p1, "this$0"    # Lshubham/intern/MainActivity;

    .prologue
    .line 91
    iput-object p1, p0, Lshubham/intern/MainActivity$2;->this$0:Lshubham/intern/MainActivity;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public onClick(Landroid/view/View;)V
    .locals 4
    .param p1, "v"    # Landroid/view/View;

    .prologue
    .line 93
    iget-object v2, p0, Lshubham/intern/MainActivity$2;->this$0:Lshubham/intern/MainActivity;

    const v3, 0x7f07008b

    invoke-virtual {v2, v3}, Lshubham/intern/MainActivity;->findViewById(I)Landroid/view/View;

    move-result-object v1

    check-cast v1, Landroid/widget/TextView;

    .line 94
    .local v1, "username":Landroid/widget/TextView;
    iget-object v2, p0, Lshubham/intern/MainActivity$2;->this$0:Lshubham/intern/MainActivity;

    const v3, 0x7f070056

    invoke-virtual {v2, v3}, Lshubham/intern/MainActivity;->findViewById(I)Landroid/view/View;

    move-result-object v0

    check-cast v0, Landroid/widget/TextView;

    .line 95
    .local v0, "password":Landroid/widget/TextView;
    const-string/jumbo v2, ""

    invoke-virtual {v1, v2}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 96
    const-string/jumbo v2, ""

    invoke-virtual {v0, v2}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 97
    return-void
.end method
